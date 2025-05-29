
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class Game {
    
    // Player, IP
    private static HashMap<String, String> players = new HashMap<>();
    private static HashMap<String, String> roles = new HashMap<>();

    // Game variables
    private String victim;
    private String savedPlayer;
    private String story;
    private static boolean gameOver = false;

    // Networking
    private boolean running = true;
    private int port = 8888;
    private DatagramSocket socket;

    private String roomInfo = "Not in a room";
    private boolean roomOpen = false;

    public void goOnline() throws IOException {
        // Start the listener thread
        new Thread(this::startListener).start();
        roomOpen = true;
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

    public void startGame() {
        roomOpen = false;

        // Initialize game logic here
        System.out.println("Game started!");
        contactAllPlayers("GAME_STARTED");
        // Add your game logic here

        assignRoles();
        while(!gameOver) {
            // Game loop
            try {
                startNightPhase();
                startDayPhase();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //gameOver = true;
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
                    //addPlayer(playerName, senderIP);
                    sendRequest(senderIP, getPlayers());
                    System.out.println("Player " + playerName + " joined the room.");
                }
                
                // requests for game logic
                if(request.equals("START_GAME")) {
                    // Do Something
                }
                if (request.startsWith("ROLE:")) {
                    String role = request.substring(5);
                    Driver.setRole(role);
                    System.out.println("Assigned role: " + role + " to player: " + Driver.getPlayerName());
                }
                if (request.equals("NIGHT_PHASE")) {
                    // Do Something
                }
                if (request.equals("DAY_PHASE")) {
                    // Do Something
                }
                if (request.startsWith("STORY:")) {
                    String storyRecieved = request.substring(6);
                    System.out.println(storyRecieved);
                }
                if (request.equals("CHOOSE_VICTIM")) {
                    // Do Something
                }
                if (request.equals("CHOOSE_SAVE")) {
                    // Do Something
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
                }
                if (request.equals("VOTE_STARTED")) {
                    // Do Something
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRoomInfo() {
        return roomInfo;
    }

    public void stop() throws IOException {
        running = false;
        socket.close();
    }
}
