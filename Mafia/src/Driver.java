
import java.awt.*;
import javax.swing.*;

public class Driver {

    // Create the CardLayout container
    static CardLayout cardLayout = new CardLayout();
    static JPanel mainPanel = new JPanel(cardLayout);
    private static Player player = new Player("Player" + (int) (Math.random() * 1000), "p1.png");
    private static String playerName = player.getName();
    private static Game game;

    public static void main(String[] args) throws Exception {
        game = new Game();
        
        Jframes();
    }

    public static Game getGame() {
        return game;
    }

    public static String getPlayerName() {
        return player.getName();
    }

    public static void setPlayerName(String name) {
        player.setName(name);
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
    }

    public static Player getPlayer() {
        return player;
    }

}