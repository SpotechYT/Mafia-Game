import java.io.*;
import java.net.*;
import java.util.*;

public class RoomDiscovery {

    public static List<String> discoverRooms() throws IOException {
        List<String> rooms = new ArrayList<>();
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        String request = "DISCOVER_ROOMS";
        byte[] sendData = request.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(
                sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8888);
        socket.send(sendPacket);

        socket.setSoTimeout(2000);  // Wait 2 seconds for responses

        try {
            while (true) {
                byte[] recvBuf = new byte[256];
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                rooms.add(response);
            }
        } catch (SocketTimeoutException e) {
            // Timeout reached, stop listening
        }

        socket.close();
        return rooms;
    }
}
