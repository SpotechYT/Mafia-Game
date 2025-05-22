import java.io.*;
import java.net.*;

public class Networking {

    private ServerSocket serverSocket;
    private boolean running = true;

    // Networkng to send data to other players
    public void sendRequest(String ipAd, String request) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] sendData = request.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ipAd), 8888);
        socket.send(sendPacket);

        System.out.println("Sent Request '" + request + "' to '" + ipAd + "'");
    }

    public Networking() throws IOException {
        serverSocket = new ServerSocket(8888);

        // Start discovery listener in background
        new Thread(this::startListener).start();
    }

    private void startListener() {
        try (DatagramSocket socket = new DatagramSocket(8888)) {
            System.out.println("Listener started on UDP port 8888");
            byte[] buf = new byte[256];
            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String request = new String(packet.getData(), 0, packet.getLength());
                String senderIP = packet.getAddress().getHostAddress();
                System.out.println("Received request: '" + request + "' from '" + senderIP + "'");

                if(request.equals("DISCOVER_ROOM")) {
                    System.out.println("Request to join this room");
                    sendRequest(senderIP, "This room is available");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        running = false;
        serverSocket.close();
    }
}
