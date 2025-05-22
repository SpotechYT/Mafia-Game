import java.io.*;
import java.net.*;

public class Networking {

    private boolean running = true;
    private int port = 8888;
    private String requestData = "";

    public String getRequestData(){
        return requestData;
    }

    // Networkng to send data to other players
    public void sendRequest(String ipAd, String request) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] sendData = request.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ipAd), port);
        socket.send(sendPacket);

        System.out.println("Sent Request '" + request + "' to '" + ipAd + "'");
    }

    public Networking() throws IOException {
        // Start discovery listener in background
        new Thread(this::startListener).start();
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
                requestData = request;

                if(request.equals("DISCOVER_ROOM")) {
                    sendRequest(senderIP, "ROOM_OPEN");
                }
                if(request.equals("ROOM_OPEN")) {
                    sendRequest(senderIP, "JOIN_ROOM:" + Driver.getplayerName());
                }
                if(request.startsWith("JOIN_ROOM:")) {
                    String playerName = request.substring(10);
                    System.out.println("Player " + playerName + " joined the room.");
                    // Add player to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        running = false;
    }
}
