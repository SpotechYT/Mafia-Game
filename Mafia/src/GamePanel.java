import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class GamePanel extends JPanel {

    public JButton backButton;

    public JTextField chatField;
    public static DefaultListModel<String> chatListModel;
    public static JList<String> chatList;
    public JButton chatButton;
    public static JButton kickButton;

    public static JPanel rightPanel;
    private boolean kickMode = false;

    private Game game = Driver.getGame();

    public GamePanel() {
        // Set layout
        setLayout(new BorderLayout());
        setBackground(java.awt.Color.BLACK);

        // Create a top panel for the back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(java.awt.Color.BLACK);
        backButton = new JButton();
        ImageIcon back = new ImageIcon("Graphics/back1.png");
        backButton.setIcon(back);
        backButton.setBackground(Color.black);

        // Add back button to the top panel
        topPanel.add(backButton);
        // Create the main panel split in two, with chat on the left and on the right, with the players side by side, evenly spaced
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(java.awt.Color.BLACK);
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(java.awt.Color.BLACK);
        rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightPanel.setBackground(java.awt.Color.BLACK);

        chatListModel = new DefaultListModel<>();
        chatList = new JList<>(chatListModel);

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        TitledBorder border = BorderFactory.createTitledBorder("Chat Room");
        border.setTitleColor(Color.WHITE);
        leftPanel.setBorder(border);
        chatField = new JTextField();
        chatField.setText("Type your message here...");
        chatField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        leftPanel.add(chatField);
        leftPanel.add(new JScrollPane(chatList), BorderLayout.CENTER);

        chatButton = new JButton();
        ImageIcon chatIcon = new ImageIcon("Graphics/send.png");
        chatButton.setIcon(chatIcon);
        chatButton.setBackground(Color.black);
        leftPanel.add(chatButton, BorderLayout.SOUTH);
        mainPanel.add(leftPanel);
        kickButton = new JButton();
        ImageIcon kickIcon = new ImageIcon("Graphics/kick.png");
        kickButton.setIcon(kickIcon);
        kickButton.setBackground(Color.black);
        rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 20px horizontal and vertical gaps
        updatePlayers();

        mainPanel.add(rightPanel);

        // // Create the bottom panel for game controls
        // JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 20px horizontal and vertical gaps
        // // Buttons to add: leave room, vote kick
        // JButton leaveRoomButton = new JButton("Leave Room");
        // bottomPanel.add(leaveRoomButton);

        // Add top panel to the top (NORTH) of the main panel
        add(topPanel, BorderLayout.NORTH);
        // Add main panel to the center of the main panel
        add(mainPanel, BorderLayout.CENTER);
        // Add bottom panel to the bottom (SOUTH) of the main panel
        //add(bottomPanel, BorderLayout.SOUTH);

        chatButton.addActionListener(e -> {
            // This is your function body
            sendChatMessage(chatField.getText());
            chatField.setText(""); // Clear the chat field after sending
            updatePlayers();
        });

        backButton.addActionListener(e -> {
            // This is your function body
            leaveRoom();
        });
        kickButton.addActionListener(e -> {
            if(game.getPlayersMap().size() < 2) {
                sendServerMessage("Not enough players to kick anyone.");
            }else{
                kickMode = true;
                kickButton.setText("Select Player to Kick");
            }
        });

        // Replace VK_YOUR_KEY with the desired key code, e.g., VK_A for 'A' key
        chatField.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                chatField.setText(""); // Clear the chat field when clicked
            }
        });
        chatField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    sendChatMessage(chatField.getText());
                    chatField.setText(""); // Clear the chat field after sending
                }
            }
        });
    }

    public static void updatePlayers(){
        //remove all existing buttons from the right panel
        rightPanel.removeAll();

        // add the players to the right panel
        for (String player : Driver.getGame().getPlayers().split("\n")) {
            JButton playerButton = new JButton(player);
            playerButton.setPreferredSize(new java.awt.Dimension(150, 50)); // Set a preferred size for each button
            // Set the button to the player name
            playerButton.setText(player);

            playerButton.addActionListener(ev -> {
                GamePanel panel = (GamePanel)SwingUtilities.getAncestorOfClass(GamePanel.class, rightPanel);
                    if (panel != null && panel.kickMode) {
                        panel.kickMode = false;
                        panel.kickButton.setText("Kick Player");
                        panel.game.contactAllPlayers("KICK:" + player);
                        panel.sendServerMessage(player + " has been kicked.");
                        updatePlayers();
                    }
            });
            rightPanel.add(playerButton);
        }
        rightPanel.add(kickButton);
        rightPanel.revalidate(); // Refresh the panel to show the new buttons
        rightPanel.repaint(); // Repaint the panel to ensure the new buttons are displayed


    }

    public void sendChatMessage(String message) {
        // Send a chat message to all players
        game.contactAllPlayers("CHAT:" + Driver.getPlayerName() + ": " + message);
    }

    public void sendServerMessage(String message) {
        // Send a chat message to all players
        game.contactAllPlayers("CHAT:" + message);
    }

    public void leaveRoom() {
        game.leaveRoom();
        sendServerMessage(Driver.getPlayerName() + " has left the room.");
    }
}
