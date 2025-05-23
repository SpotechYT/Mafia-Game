
import java.awt.*;
import javax.swing.*;

public class Driver {

    // Create the CardLayout container
    static CardLayout cardLayout = new CardLayout();
    static JPanel mainPanel = new JPanel(cardLayout);

    private static String playerName = "Player" + (int) (Math.random() * 1000);

    public static void main(String[] args) throws Exception {
        Jframes();

        //Game game = new Game();
        //game.startGame();
    }

    public static String getplayerName() {
        return playerName;
    }

    public static void setplayerName(String name) {
        playerName = name;
    }

    public static void Jframes() throws Exception {
        // Create the frame
        JFrame frame = new JFrame("Mafia");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create instances of panels
        MainMenu mainMenu = new MainMenu();
        JoinRoom joinRoom = new JoinRoom();
        GamePanel gamePanel = new GamePanel();
        SettingPanel settingPanel = new SettingPanel();

        // Add panels to the card layout
        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(joinRoom, "JoinRoom");
        mainPanel.add(gamePanel, "GamePanel");
        mainPanel.add(settingPanel, "SettingPanel");

        linkButton(mainMenu.playButton, "JoinRoom");
        linkButton(mainMenu.settingsButton, "SettingPanel");
        linkButton(joinRoom.backButton, "MainMenu");
        linkButton(settingPanel.backButton, "MainMenu");

        // Set the content pane
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    public static void linkButton(JButton button, String panelName) {
        button.addActionListener(e -> cardLayout.show(mainPanel, panelName));

        //testteset
    }
}