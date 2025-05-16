
import java.awt.*;
import javax.swing.*;

public class Driver {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Mafia");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create the CardLayout container
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Create instances of panels
        MainMenu mainMenu = new MainMenu();
        GamePanel gamePanel = new GamePanel();
        SettingPanel settingPanel = new SettingPanel();

        // Add panels to the card layout
        mainPanel.add(mainMenu, "MainMenu");
        mainPanel.add(gamePanel, "GamePanel");
        mainPanel.add(settingPanel, "SettingPanel");

        // Switch to GamePanel when "Play" is clicked
        mainMenu.playButton.addActionListener(e -> cardLayout.show(mainPanel, "GamePanel"));

        // Switch to SettingPanel when "Settings" is clicked
        mainMenu.settingsButton.addActionListener(e -> cardLayout.show(mainPanel, "SettingPanel"));

        // Go back to MainMenu when "Back" is clicked in the game
        gamePanel.backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        // Go back to MainMenu when "Back" is clicked in settings
        settingPanel.backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        // Set the content pane
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
