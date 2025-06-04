
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class Game {
    
    // Player, IP
    private static HashMap<String, String> players = new HashMap<>();
    private HashMap<String, String> roles = new HashMap<>();

    // Game variables
    private String victim;
    private String savedPlayer;
    private String story;
    private boolean gameOver = false;
    private String currentMode;

    // Networking
    private boolean running = true;
    private int port = 8888;
    private DatagramSocket socket;

    private String roomInfo = "Not in a room";
    private boolean roomOpen = false;

    public void goOnline() throws IOException {
        // Start the listener thread
        roomOpen = true;
        new Thread(this::startListener).start();
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public static void addPlayer(String player, String ip) {
        players.put(player, ip);
        JoinRoom.addPlayerToList(player + ":" + ip);
    }

    public String getPlayers() {
        StringBuilder playerList = new StringBuilder();
        for (String player : players.keySet()) {
            playerList.append(player).append("\n");
        }
        return playerList.toString();
    }

    public String getPlayersAndIPs() {
        StringBuilder playerList = new StringBuilder();
        for (String player : players.keySet()) {
            playerList.append(player).append(":").append(players.get(player)).append("\n");
        }
        return playerList.toString();
    }

    public void startGame() {
        roomOpen = false;

        // Initialize game logic here
        System.out.println("Game started!");
        Driver.showGamePanel();
        contactAllPlayers("GAME_STARTED");

        new Thread(this::gameLogic).start();
    }

    private void gameLogic(){
        assignRoles();
        while (!gameOver) {
            // Game loop
            try {
                startNightPhase();
                startDayPhase();
            } catch (Exception e) {
                e.printStackTrace();
            }
            gameOver = true;
        }
    }

    public void assignRoles() {
        // Logic to assign roles to players
        int mafia = (int)(Math.random() * players.size());
        int doctor = (int)(Math.random() * players.size());
        while(mafia == doctor) {
            doctor = (int)(Math.random() * players.size());
        }

        for(String player : players.keySet()) {
            if (mafia == 0) {
                roles.put(player, "Mafia");
            } else if (doctor == 0) {
                roles.put(player, "Doctor");
            } else {
                roles.put(player, "Citizen");
            }
            mafia--;
            doctor--;
        }

        System.out.println("Roles assigned");
        distributeRoles();
    }

    public void distributeRoles() {
        // Send a request to each player with their updated player role
        for (String player : players.keySet()) {
            String role = roles.get(player);
            String ip = players.get(player);
            try {
                System.out.println("Distributing role " + role + " to player " + player + " at IP " + ip);
                sendRequest(ip, "ROLE:" + role);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Roles distributed to players");
    }

    public void startNightPhase() throws Exception {
        victim = "";
        savedPlayer = "";
        story = "";

        // Logic to start the night phase
        System.out.println("Night phase started");

        // Tell the players that the night phase has started
        contactAllPlayers("NIGHT_PHASE");

        // Tell the Mafia to choose a victim
        sendRequest(roles.get("Mafia"), "CHOOSE_VICTIM");

        // Tell the Doctor to choose a player to save
        sendRequest(roles.get("Doctor"), "CHOOSE_SAVE");

        System.out.println("Waiting for players to choose victim and saved player...");

        // Once the victim and saved player are chosen, process the results
        while (victim.isEmpty() || savedPlayer.isEmpty()) {
            // Wait for the victim and saved player to be set
            try {
                Thread.sleep(500); // Sleep for a second before checking again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Generate Results
        if (victim.equals(savedPlayer)){
            // Saved Story
            story = MafiaScenarioGenerator.getScenario(victim, true);
        } else {
            // Deat Story
            story = MafiaScenarioGenerator.getScenario(victim, false);
        }
    }

    public void startDayPhase() {
        // Logic to start the day phase
        System.out.println("Day phase started");

        // Notify players about the start of the day phase
        contactAllPlayers("DAY_PHASE");
        // Distribute the story to all players
        contactAllPlayers("STORY:" + story);

        // Give the players time to discuss
        contactAllPlayers("DISCUSSION_STARTED");

        try {
            Thread.sleep(30000); // 30 seconds for discussion
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // After discussion, ask players to vote
        contactAllPlayers("VOTE_STARTED");

        // Wait to recieve all votes
        // Sum them up
        // Kick the player with the most votes
        // Display the results
        System.out.println("Day phase ended");
    }

    public void endGame() throws IOException {
        // Logic to end the game
        System.out.println("Game ended");
        gameOver = true;
        stop();
    }

    public void contactAllPlayers(String message) {
        // Send a message to all players
        for (String player : players.keySet()) {
            String ip = players.get(player);
            try {
                sendRequest(ip, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Message sent to all players: " + message);
    }

    // Networkng to send data to other players
    public void sendRequest(String ipAd, String request) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] sendData = request.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ipAd), port);
        socket.send(sendPacket);

        System.out.println("Sent Request '" + request + "' to '" + ipAd + "'");
    }

    private void startListener() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Listener started on UDP port " + port);
            byte[] buf = new byte[256];
            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String request = new String(packet.getData(), 0, packet.getLength());
                String senderIP = packet.getAddress().getHostAddress();
                System.out.println("Received request: '" + request + "' from '" + senderIP + "'");

                // requests for room management
                if(request.equals("DISCOVER_ROOM")) {
                    if(roomOpen == true){
                        sendRequest(senderIP, "ROOM_OPEN");
                    } else{
                        sendRequest(senderIP, "ROOM_CLOSED");
                    }
                    roomInfo = senderIP + ":" + request;
                }
                if(request.equals("ROOM_OPEN")) {
                    sendRequest(senderIP, "JOIN_ROOM:" + Driver.getPlayerName());
                }
                if(request.equals("ROOM_CLOSED")) {
                    roomInfo = "Room is Closed";
                }
                if(request.startsWith("JOIN_ROOM:")) {
                    String playerName = request.substring(10);
                    addPlayer(playerName, senderIP);
                    sendRequest(senderIP, "PLAYERS:" + getPlayersAndIPs());
                    System.out.println("Player " + playerName + " joined the room.");
                }
                if(request.startsWith("PLAYERS:")) {
                    String playersList = request.substring(8);
                    JoinRoom.clearPlayerList();
                    for (String player : playersList.split("\n")) {
                        String name = player.split(":")[0];
                        String ip = player.split(":")[1];
                        addPlayer(name, ip);
                    }
                }
                if(request.equals("PLAYER_LEFT")) {
                    String playerName = players.get(senderIP);
                    players.remove(playerName);
                    try{
                        roles.remove(playerName);
                    } catch (Exception e) {
                        System.out.println("No role assigned to player: " + playerName);
                    }
                    JoinRoom.removePlayerFromList(playerName);
                    GamePanel.updatePlayers();
                    System.out.println("Player " + playerName + " left the room.");
                }
                
                // requests for game logic
                if(request.equals("GAME_STARTED")) {
                    Driver.showGamePanel();
                }
                if (request.startsWith("ROLE:")) {
                    String role = request.substring(5);
                    Driver.setRole(role);
                    GamePanel.setRoleText("Your Role: " + role);
                    System.out.println("Assigned role: " + role + " to player: " + Driver.getPlayerName());
                }
                if(request.startsWith("CHAT:")) {
                    String chatMessage = request.substring(5);
                    GamePanel.chatListModel.addElement(chatMessage);
                    // Scroll to the bottom of the chat list
                    GamePanel.chatList.ensureIndexIsVisible(GamePanel.chatListModel.getSize() - 1);
                }
                if (request.equals("NIGHT_PHASE")) {
                    // Do Something
                    GamePanel.setGameText("The Town has went to sleep...ZZZ...");
                    currentMode = "NIGHT_PHASE";
                }
                if (request.equals("DAY_PHASE")) {
                    // Do Something
                    GamePanel.setGameText("The Town has woken up! The sun is shining and the day has begun!");
                    currentMode = "DAY_PHASE";
                }
                if (request.startsWith("STORY:")) {
                    String storyRecieved = request.substring(6);
                    GamePanel.setGameText(storyRecieved);
                    System.out.println(storyRecieved);
                }
                if (request.equals("CHOOSE_VICTIM")) {
                    // Do Something
                    GamePanel.setGameText("Mafia, please choose a victim");
                    currentMode = "CHOOSE_VICTIM";
                }
                if (request.equals("CHOOSE_SAVE")) {
                    // Do Something
                    GamePanel.setGameText("Doctor, please choose a player to save");
                    currentMode = "CHOOSE_SAVE";
                }
                if (request.startsWith("VICTIM:")) {
                    victim = request.substring(7);
                    System.out.println("Victim chosen: " + victim);
                }
                if (request.startsWith("SAVE:")) {
                    savedPlayer = request.substring(5);
                    System.out.println("Saved player chosen: " + savedPlayer);
                }
                if (request.equals("DISCUSSION_STARTED")) {
                    // Do Something
                    GamePanel.setGameText("Discussion has started! Discuss with other players to find the Mafia");
                }
                if (request.equals("VOTE_STARTED")) {
                    // Do Something
                    GamePanel.setGameText("Voting has started! Select the player you would like to remove");
                    currentMode = "VOTE";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String leaveRoom(){
        if (roomOpen) {
            roomOpen = false;
            roomInfo = "Not in a room";
            players.clear();
            roles.clear();
            JoinRoom.clearPlayerList();
            contactAllPlayers("PLAYER_LEFT");
            return "You have left the room.";
        } else {
         return "You are not in a room.";
        }
    }

    public String getRoomInfo() {
        return roomInfo;
    }

    public void stop() throws IOException {
        running = false;
        leaveRoom();
        socket.close();
    }
}
