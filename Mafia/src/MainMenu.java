import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenu extends JPanel {

    public JButton playButton;
    public JButton settingsButton;

    public MainMenu() {
        setLayout(new BorderLayout());

        setBackground(java.awt.Color.BLACK);

        JLabel label = new JLabel("The Mafia Game", JLabel.CENTER);
        label.setForeground(java.awt.Color.WHITE);
        add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(java.awt.Color.BLACK);
        playButton = new JButton("Play");
        settingsButton = new JButton("Settings");

        buttonPanel.add(playButton);
        buttonPanel.add(settingsButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

}
