
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class JoinRoom extends JPanel {

    public JButton backButton;
    public JLabel ipLabel;

    public DefaultListModel<String> roomListModel;
    public JList<String> roomList;
    public JButton refreshButton;

    public JTextField roomNameField;
    public JTextField ipAdField;
    public JButton createRoomButton;
    public JButton startGameButton;

    public static DefaultListModel<String> playerListModel;
    public JList<String> playerList;

    private Game game = Driver.getGame();

    public JoinRoom() {
        setLayout(new BorderLayout());

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout());

        backButton = new JButton();
        ImageIcon backIcon = new ImageIcon("Graphics/back1.png");
        backButton.setIcon(backIcon);
        backButton.setBackground(Color.black);
        backButton.setBorder(null);
        topPanel.add(backButton, BorderLayout.WEST);

        ipLabel = new JLabel("Your IP: " + Driver.getYourIp(), SwingConstants.CENTER);
        ipLabel.setFont(ipLabel.getFont().deriveFont(Font.BOLD, 14f));
        topPanel.add(ipLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL (LEFT + RIGHT) ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        // LEFT: Discovered Rooms List
        JPanel leftPanel = new JPanel(new BorderLayout());

        roomListModel = new DefaultListModel<>();
        roomList = new JList<>(roomListModel);

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Available Rooms"));
        ipAdField = new JTextField();
        ipAdField.setText("Enter IP address");
        ipAdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        leftPanel.add(new JLabel("IP address:"));
        leftPanel.add(ipAdField);
        leftPanel.add(new JScrollPane(roomList), BorderLayout.CENTER);

        refreshButton = new JButton();
        ImageIcon refreshIcon = new ImageIcon("Graphics/connect.png");
        refreshButton.setIcon(refreshIcon);
        refreshButton.setBackground(Color.black);
        refreshButton.setBorder(null);
        leftPanel.add(refreshButton, BorderLayout.SOUTH);

        centerPanel.add(leftPanel);

        // RIGHT: Host Room Form + Player List
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Host a Room"));

        // Room creation fields
        createRoomButton = new JButton();
        ImageIcon createRoomIcon = new ImageIcon("Graphics/online.png");
        createRoomButton.setIcon(createRoomIcon);
        createRoomButton.setBackground(Color.black);
        createRoomButton.setBorder(null);

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

        startGameButton = new JButton();
        ImageIcon startGameIcon = new ImageIcon("Graphics/startGame.png");
        startGameButton.setIcon(startGameIcon);
        startGameButton.setBackground(Color.black);
        startGameButton.setBorder(null);

        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(startGameButton);
        rightPanel.add(Box.createVerticalStrut(20));

        rightPanel.add(Box.createVerticalGlue());

        centerPanel.add(rightPanel);

        refreshButton.addActionListener(e -> {
            // This is your function body
            onRefreshRooms();
        });

        createRoomButton.addActionListener(e -> {
            // This is your function body
            onCreateRoom();
        });

        backButton.addActionListener(e -> {
            // This is your function body
            game.leaveRoom();
        });

        add(centerPanel, BorderLayout.CENTER);
    }

    // ====== Utility Methods ======
    public void addDiscoveredRoom(String roomInfo) {
        String entry = roomInfo;
        if (!roomListModel.contains(entry)) {
            roomListModel.addElement(entry);
        }
    }

    public void clearDiscoveredRooms() {
        roomListModel.clear();
    }

    public static void addPlayerToList(String playerName) {
        if (!playerListModel.contains(playerName)) {
            playerListModel.addElement(playerName);
        }

        GamePanel.updatePlayers();
    }

    public static void removePlayerFromList(String playerName) {
        playerListModel.removeElement(playerName);
    }

    public static void clearPlayerList() {
        playerListModel.clear();
    }

    public void onRefreshRooms() {
        System.out.println("Refresh");
        clearPlayerList();
        clearDiscoveredRooms();

        try {
            String ip = ipAdField.getText();
            String request = "DISCOVER_ROOM";
            game.sendRequest(ip, request);
            addDiscoveredRoom(game.getRoomInfo());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to discover rooms.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onCreateRoom() {
        String name = Driver.getPlayerName();
        game.addPlayer(name, Driver.getYourIp());
        System.out.println("Going Online with name" + name);

        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Room name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            game.goOnline();
        } catch (IOException ex) {
            ex.printStackTrace(); // Log the error
            JOptionPane.showMessageDialog(this, "Failed to create room.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
