
import java.awt.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import javax.swing.*;

public class Driver {

    // Create the CardLayout container
    static CardLayout cardLayout = new CardLayout();
    static JPanel mainPanel = new JPanel(cardLayout);
    private static String playerName = "Player" + (int) (Math.random() * 1000);
    private static String role;
    private static Game game;

    public static void main(String[] args) throws Exception {
        game = new Game();
        
        Jframes();
    }

    public static Game getGame() {
        return game;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String givenRole) {
        role = givenRole;
    }

    public static String getPlayerName() {
        return playerName;
    }

    public static void setPlayerName(String name) {
        playerName = name;
    }

    public static String getYourIp() {
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
        linkButton(joinRoom.startGameButton, "GamePanel");
        linkButton(gamePanel.backButton, "MainMenu");
        linkButton(settingPanel.backButton, "MainMenu");

        // Set the content pane
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    public static void linkButton(JButton button, String panelName) {
        button.addActionListener(e -> cardLayout.show(mainPanel, panelName));
    }

}