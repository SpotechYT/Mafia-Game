
import java.awt.*;
import java.net.*;
import java.util.Enumeration;
import java.util.function.Consumer;
import javax.swing.*;

public class JoinRoom extends JPanel {

    public JButton backButton;
    public JLabel ipLabel;

    public DefaultListModel<String> roomListModel;
    public JList<String> roomList;
    public JButton refreshButton;

    public JTextField roomNameField;
    public JButton createRoomButton;

    public DefaultListModel<String> playerListModel;
    public JList<String> playerList;

    public JoinRoom() {
        setLayout(new BorderLayout());

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout());

        backButton = new JButton("Back");
        topPanel.add(backButton, BorderLayout.WEST);

        ipLabel = new JLabel("Your IP: " + getYourIp(), SwingConstants.CENTER);
        ipLabel.setFont(ipLabel.getFont().deriveFont(Font.BOLD, 14f));
        topPanel.add(ipLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL (LEFT + RIGHT) ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        // LEFT: Discovered Rooms List
        JPanel leftPanel = new JPanel(new BorderLayout());
        roomListModel = new DefaultListModel<>();
        roomList = new JList<>(roomListModel);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Available Rooms"));
        leftPanel.add(new JScrollPane(roomList), BorderLayout.CENTER);

        refreshButton = new JButton("Refresh");
        leftPanel.add(refreshButton, BorderLayout.SOUTH);

        centerPanel.add(leftPanel);

        // RIGHT: Host Room Form + Player List
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Host a Room"));

        // Room creation fields
        roomNameField = new JTextField();
        roomNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        createRoomButton = new JButton("Create Room");

        rightPanel.add(new JLabel("Room Name:"));
        rightPanel.add(roomNameField);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createRoomButton);
        rightPanel.add(Box.createVerticalStrut(20));

        // Player List
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        JScrollPane playerScrollPane = new JScrollPane(playerList);
        playerScrollPane.setPreferredSize(new Dimension(200, 150));

        rightPanel.add(new JLabel("Players in Room:"));
        rightPanel.add(playerScrollPane);

        rightPanel.add(Box.createVerticalGlue());

        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    // ====== Utility Methods ======
    // public String getYourIp() {
    //     try {
    //         InetAddress localHost = InetAddress.getLocalHost();
    //         String ipAddress = localHost.getHostAddress();
    //         return ipAddress;
    //     } catch (UnknownHostException e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }
    public String getYourIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // Skip loopback and down interfaces
                if (iface.isLoopback() || !iface.isUp()) continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setYourIp(String ip) {
        ipLabel.setText("Your IP: " + ip);
    }

    public void addDiscoveredRoom(String roomName, String ip) {
        String entry = roomName + " - " + ip;
        if (!roomListModel.contains(entry)) {
            roomListModel.addElement(entry);
        }
    }

    public void clearDiscoveredRooms() {
        roomListModel.clear();
    }

    public String getSelectedRoomIp() {
        String selected = roomList.getSelectedValue();
        if (selected != null && selected.contains(" - ")) {
            return selected.split(" - ")[1].trim();
        }
        return null;
    }

    public String getEnteredRoomName() {
        return roomNameField.getText().trim();
    }

    public void addPlayerToList(String playerName) {
        if (!playerListModel.contains(playerName)) {
            playerListModel.addElement(playerName);
        }
    }

    public void removePlayerFromList(String playerName) {
        playerListModel.removeElement(playerName);
    }

    public void clearPlayerList() {
        playerListModel.clear();
    }

    // Optional callbacks
    public void onRefreshRooms(Runnable callback) {
        refreshButton.addActionListener(e -> {
            if (callback != null) {
                callback.run();
            }
        });
    }

    public void onCreateRoom(Consumer<String> callback) {
        createRoomButton.addActionListener(e -> {
            String name = getEnteredRoomName();
            if (!name.isEmpty() && callback != null) {
                callback.accept(name);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a room name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
