
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class Game {
    private static boolean gameOver = false;
    // Player, IP
    private static HashMap<Player, String> players = new HashMap<>();

    // Victim and saved player
    private String victim;
    private String savedPlayer;
    private String story;

    // Networking
    private boolean running = true;
    private int port = 8888;
    private DatagramSocket socket;

    private String roomInfo = "Not in a room";

    public void goOnline() throws IOException {
        // Start the listener thread
        new Thread(this::startListener).start();
    }

    public static void addPlayer(Player player, String ip) {
        players.put(player, ip);
        JoinRoom.addPlayerToList(player.getName() + ":" + ip);
    }

    public String getPlayers() {
        StringBuilder playerList = new StringBuilder();
        for (Player player : players.keySet()) {
            playerList.append(player.getName()).append("\n");
        }
        return playerList.toString();
    }

    public void startGame() {
        // Initialize game logic here
        System.out.println("Game started!");
        // Add your game logic here

        assignRoles();
        while(!gameOver) {
            // Game loop
            // Check for game over conditions
            // If game over, set gameOver = true;
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

        System.out.println("Roles assigned");
    }

    public void distributeRoles() {
        // Send a request to each player with their updated player role

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
        //sendRequest(roles.get("Mafia"), "CHOOSE_VICTIM");

        // Tell the Doctor to choose a player to save
        //sendRequest(roles.get("Doctor"), "CHOOSE_PLAYER_TO_SAVE");

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
        if (victim == savedPlayer){
            // Saved Story
            story = MafiaScenarioGenerator.getScenario(victim, true);
        } else {
            // Deat Story
            story = MafiaScenarioGenerator.getScenario(victim, false);
        }

        contactAllPlayers("NIGHT_PHASE_ENDED");
    }

    public void startDayPhase() {
        // Logic to start the day phase
        System.out.println("Day phase started");

        // Notify players about the start of the day phase
        contactAllPlayers("DAY_PHASE");
        // Distribute the story to all players
        contactAllPlayers("STORY:" + story);

        // Give the players time to discuss
        contactAllPlayers("DISCUSS");
        try {
            Thread.sleep(30000); // 30 seconds for discussion
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // After discussion, ask players to vote
        contactAllPlayers("VOTE");

        // Wait to recieve all votes
        // Sum them up
        // Kick the player with the most votes
        // Display the results
        contactAllPlayers("DAY_PHASE_ENDED");
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
        for (Player player : players.keySet()) {
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

                // This is where you handle the requests
                if(request.equals("DISCOVER_ROOM")) {
                    sendRequest(senderIP, "ROOM_IS_OPEN");
                    roomInfo = senderIP + ":" + request;
                }
                if(request.equals("ROOM_IS_OPEN")) {
                    sendRequest(senderIP, "JOIN_ROOM:" + Driver.getPlayerName());
                }
                if(request.startsWith("JOIN_ROOM:")) {
                    String playerName = request.substring(10);
                    //addPlayer(playerName, senderIP);
                    sendRequest(senderIP, getPlayers());
                    System.out.println("Player " + playerName + " joined the room.");
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
