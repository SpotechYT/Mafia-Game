import java.io.*;
import java.net.*;

public class RoomHost {

    private ServerSocket serverSocket;
    private String roomName;
    private int tcpPort;
    private boolean running = true;

    public RoomHost(String roomName, int tcpPort) throws IOException {
        this.roomName = roomName;
        this.tcpPort = tcpPort;
        serverSocket = new ServerSocket(tcpPort);

        // Start discovery listener in background
        new Thread(this::startDiscoveryListener).start();
    }

    private void startDiscoveryListener() {
        try (DatagramSocket socket = new DatagramSocket(8888)) {
            byte[] buf = new byte[256];
            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String request = new String(packet.getData(), 0, packet.getLength());
                if (request.equals("DISCOVER_ROOMS")) {
                    String response = roomName + ":" + InetAddress.getLocalHost().getHostAddress() + ":" + tcpPort;
                    byte[] responseData = response.getBytes();
                    DatagramPacket responsePacket = new DatagramPacket(
                            responseData, responseData.length, packet.getAddress(), packet.getPort());
                    socket.send(responsePacket);
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

    // Accept client connections here if needed
}
