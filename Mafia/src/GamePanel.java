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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class GamePanel extends JPanel {

    public JButton backButton;

    public JTextField chatField;
    public static DefaultListModel<String> chatListModel;
    public static JList<String> chatList;
    public JButton chatButton;

    public static JLabel gameText;
    public static JLabel roleText;

    public static JPanel rightPanel;

    private static Game game = Driver.getGame();

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

        rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 20px horizontal and vertical gaps
        roleText = new JLabel("No Role Assigned");
        roleText.setForeground(Color.WHITE);
        rightPanel.add(roleText);
        gameText = new JLabel("Waiting for other players to join...");
        gameText.setForeground(Color.WHITE);
        rightPanel.add(gameText);
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
        });

        backButton.addActionListener(e -> {
            // This is your function body
            leaveRoom();
        });

        // Player Buttons
    }

    public static void updatePlayers() {
        // remove all existing buttons from the right panel
        rightPanel.removeAll();

        // add the players to the right panel
        for (String player : Driver.getGame().getPlayers().split("\n")) {
            JButton playerButton = new JButton(player);
            playerButton.setPreferredSize(new java.awt.Dimension(150, 50));

            playerButton.addActionListener(e -> {
                System.out.println("Clicked on player: " + player);
                String currentMode = game.getCurrentMode();
                switch(currentMode) {
                    case "VOTE":
                        // If in voting mode, send a vote for the player
                        game.contactAllPlayers("VOTE:" + player);
                        break;
                    case "KICK":
                        // If in kicking mode, send a kick request for the player
                        game.contactAllPlayers("KICK:" + player);
                        break;
                    case "CHOSE_SAVE":
                        // If in choose save mode, send a save request for the player
                        game.contactAllPlayers("SAVE:" + player);
                        break;
                    case "CHOSE_VICTIM":
                        // If in choose victim mode, send a victim request for the player
                        game.contactAllPlayers("VICTIM:" + player);
                        break;
                    default:
                        // Default action can be defined here if needed
                        System.out.println("No action defined for current mode: " + currentMode);
                }
            });

            rightPanel.add(playerButton);
        }

        // Refresh the panel after adding components
        rightPanel.revalidate();
        rightPanel.repaint();
    }    

    public static void setGameText(String text) {
        // Set the game text to the given text
        gameText.setText(text);
    }

    public static void setRoleText(String text) {
        // Set the role text to the given text
        roleText.setText(text);
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
