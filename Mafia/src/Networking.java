import java.io.*;
import java.net.*;

public class Networking {

    private ServerSocket serverSocket;
    private boolean running = true;

    // Networkng to send data to other players
    public static String sendRequest(String ipAd, String request) throws IOException {
        String resp = "";
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] sendData = request.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ipAd), 8888);
        socket.send(sendPacket);

        socket.setSoTimeout(2000);  // Wait 2 seconds for responses

        try {
            while (true) {
                byte[] recvBuf = new byte[256];
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                resp = response;
            }
        } catch (SocketTimeoutException e) {
            // Timeout reached, stop listening
            System.err.println(e.getMessage());
        }

        socket.close();
        return resp;
    }

    public Networking() throws IOException {
        serverSocket = new ServerSocket(8888);

        // Start discovery listener in background
        new Thread(this::startListener).start();
    }

    private void startListener() {
        try (DatagramSocket socket = new DatagramSocket(8888)) {
            System.out.println("Discovery listener started on UDP port 8888");
            byte[] buf = new byte[256];
            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String request = new String(packet.getData(), 0, packet.getLength());
                String senderIP = packet.getAddress().getHostAddress();
                System.out.println("Received request: '" + request + "' from '" + senderIP + "'");

                if(request.equals("DISCOVER_ROOM")) {
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
