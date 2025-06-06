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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class GamePanel extends JPanel {

    public JButton backButton;

    public JTextField chatField;
    public static DefaultListModel<String> chatListModel;
    public static JList<String> chatList;
    public JButton chatButton;
    public static JButton kickButton;
    public static ImageIcon kickIcon;
    public static ImageIcon kickIcon2;
    public static String prevMode;

    public static JTextArea gameText;
    public static JLabel roleText;

    public static JPanel rightPanel;
    public static JPanel brPanel;

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
        JoinRoom.applyColor(chatField);
        JoinRoom.applyColor(chatList);
        JScrollPane chatScrollPane = new JScrollPane(chatList);
        JoinRoom.applyColor(chatScrollPane);
        leftPanel.add(chatField);
        leftPanel.add(chatScrollPane, BorderLayout.CENTER);

        

        chatButton = new JButton();
        ImageIcon chatIcon = new ImageIcon("Graphics/send.png");
        chatButton.setIcon(chatIcon);
        chatButton.setBackground(Color.black);
        leftPanel.add(chatButton, BorderLayout.SOUTH);

        mainPanel.add(leftPanel);
        kickButton = new JButton();
        kickIcon = new ImageIcon("Graphics/kick.png");
        kickIcon2 = new ImageIcon("Graphics/selectPlayer.png");
        kickButton.setIcon(kickIcon);
        kickButton.setBorder(null);
        kickButton.setBackground(Color.black);


        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        JPanel trPanel = new JPanel();
        trPanel.setLayout(new BoxLayout(trPanel, BoxLayout.Y_AXIS));
        trPanel.setBackground(Color.BLACK);

        brPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        brPanel.setBackground(Color.BLACK);

        roleText = new JLabel("No Role Assigned");
        roleText.setForeground(Color.WHITE);
        trPanel.add(roleText);

        // Use JTextArea for wrapping
        gameText = new JTextArea("Waiting for other players to join...");
        gameText.setLineWrap(true);
        gameText.setWrapStyleWord(true);
        gameText.setEditable(false);
        gameText.setFocusable(false);
        gameText.setOpaque(false); // Transparent background
        gameText.setForeground(Color.WHITE);
        gameText.setMaximumSize(new Dimension(300, 100)); // Limit width so it wraps

        trPanel.add(gameText);

        updatePlayers();

        rightPanel.add(trPanel);
        rightPanel.add(brPanel);
        mainPanel.add(rightPanel);

        // // Create the bottom panel for game controls
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 20px horizontal and vertical gaps
        // Buttons to add: leave room, vote kick
        bottomPanel.setBackground(java.awt.Color.BLACK);
        JButton voiceChatButton = new JButton("Start Voice Chat");
        voiceChatButton.setBackground(Color.black);
        voiceChatButton.setForeground(Color.white);
        voiceChatButton.setPreferredSize(new Dimension(160, 40));
        bottomPanel.add(voiceChatButton);
        bottomPanel.add(kickButton);

        // Add top panel to the top (NORTH) of the main panel
        add(topPanel, BorderLayout.NORTH);
        // Add main panel to the center of the main panel
        add(mainPanel, BorderLayout.CENTER);
        // Add bottom panel to the bottom (SOUTH) of the main panel
        add(bottomPanel, BorderLayout.SOUTH);

        

        chatButton.addActionListener(e -> {
            // This is your function body
            sendChatMessage(chatField.getText());
            chatField.setText(""); // Clear the chat field after sending
        });

        backButton.addActionListener(e -> {
            // This is your function body
            leaveRoom();
        });
        kickButton.addActionListener(e -> {
            prevMode = game.getCurrentMode();
            kickButton.setIcon(kickIcon2);
            game.setCurrentMode("KICK");
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
        voiceChatButton.addActionListener(e -> {
            if (game.isVoiceChatEnabled()) {
                game.stopVoiceChat(Driver.getPlayerName());
                voiceChatButton.setText("Start Voice Chat");
            } else {
                game.startVoiceChat(Driver.getPlayerName());
                voiceChatButton.setText("Stop Voice Chat");
            }
        });
    }

    public static void updatePlayers() {
        // Call the method with the right panel
        addPlayers(brPanel);
    }

    public static void addPlayers(JPanel thePanel) {
        // remove all existing buttons from the right panel
        thePanel.removeAll();

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
                        game.setCurrentMode("NONE");
                        break;
                    case "KICK":
                        // If in kicking mode, send a kick request for the player
                        game.contactAllPlayers("KICK:" + player);
                        kickButton.setIcon(kickIcon); // Reset the kick button icon
                        game.setCurrentMode(prevMode); // Reset to previous mode after kick
                        break;
                    case "CHOOSE_SAVE":
                        // If in choose save mode, send a save request for the player
                        game.contactAllPlayers("SAVE:" + player);
                        game.setCurrentMode("NONE");
                        break;
                    case "CHOOSE_VICTIM":
                        // If in choose victim mode, send a victim request for the player
                        game.contactAllPlayers("VICTIM:" + player);
                        game.setCurrentMode("NONE");
                        break;
                    default:
                        // Default action can be defined here if needed
                        System.out.println("No action defined for current mode: " + currentMode);
                }
            });

            thePanel.add(playerButton);
        }
        //rightPanel.add(kickButton);

        // Refresh the panel after adding components
        thePanel.revalidate();
        thePanel.repaint();
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
        game.contactAllPlayers("PLAYER_LEFT");
        sendServerMessage(Driver.getPlayerName() + " has left the room.");
    }
}
