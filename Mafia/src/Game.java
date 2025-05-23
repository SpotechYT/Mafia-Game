
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class Game {
    boolean gameOver = false;
    // Name, IP
    HashMap<String, String> players = new HashMap<>();
    // Player, Role
    HashMap<String, String> roles = new HashMap<>();

    // Networking
    private boolean running = true;
    private int port = 8888;
    private DatagramSocket socket;

    private String roomInfo = "Not in a room";

    public void goOnline() throws IOException {
        // Start the listener thread
        new Thread(this::startListener).start();
    }

    public void addPlayer(String name, String ip) {
        players.put(name, ip);
    }

    public String getPlayers() {
        StringBuilder playerList = new StringBuilder();
        for (String player : players.keySet()) {
            playerList.append(player).append("\n");
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

        System.out.println("Roles assigned!");
    }

    public void startNightPhase() {
        // Logic to start the night phase
        System.out.println("Night phase started!");
    }

    public void startDayPhase() {
        // Logic to start the day phase
        System.out.println("Day phase started!");
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
                    sendRequest(senderIP, "JOIN_ROOM:" + Driver.getplayerName());
                }
                if(request.startsWith("JOIN_ROOM:")) {
                    String playerName = request.substring(10);
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
