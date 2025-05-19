
import java.awt.*;
import javax.swing.*;

public class JoinRoom extends JPanel {

    // Top UI components
    public JButton backButton;
    public JLabel ipLabel;

    // Room discovery (left panel)
    public DefaultListModel<String> roomListModel;
    public JList<String> roomList;
    public JButton refreshButton;

    // Host room UI (right panel)
    public JTextField roomNameField;
    public JButton createRoomButton;

    public JoinRoom() {
        setLayout(new BorderLayout());

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout());

        backButton = new JButton("Back");
        topPanel.add(backButton, BorderLayout.WEST);

        ipLabel = new JLabel("Your IP: 0.0.0.0", SwingConstants.CENTER);
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

        // RIGHT: Host Room Form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Host a Room"));

        roomNameField = new JTextField();
        roomNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        createRoomButton = new JButton("Create Room");

        rightPanel.add(new JLabel("Room Name:"));
        rightPanel.add(roomNameField);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createRoomButton);
        rightPanel.add(Box.createVerticalGlue());

        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    // ====== METHODS TO IMPLEMENT YOUR NETWORKING LOGIC ======
    /**
     * Set the user's IP in the top label
     */
    public void setYourIp(String ip) {
        ipLabel.setText("Your IP: " + ip);
    }

    /**
     * Add a discovered room to the list
     */
    public void addDiscoveredRoom(String roomName, String ip) {
        String entry = roomName + " - " + ip;
        if (!roomListModel.contains(entry)) {
            roomListModel.addElement(entry);
        }
    }

    /**
     * Clear all discovered rooms
     */
    public void clearDiscoveredRooms() {
        roomListModel.clear();
    }

    /**
     * Get selected room's IP from the list
     */
    public String getSelectedRoomIp() {
        String selected = roomList.getSelectedValue();
        if (selected != null && selected.contains(" - ")) {
            return selected.split(" - ")[1].trim();
        }
        return null;
    }

    /**
     * Get entered room name for hosting
     */
    public String getEnteredRoomName() {
        return roomNameField.getText().trim();
    }
}
