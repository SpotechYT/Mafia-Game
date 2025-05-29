import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

public class MainMenu extends JPanel {

    public JButton playButton;
    public JButton settingsButton;

    public MainMenu() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("The Mafia Game", JLabel.CENTER);
        add(label, BorderLayout.NORTH);
        

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        playButton = new JButton();
        
       

        ImageIcon icon = new ImageIcon("Graphics/playButton.png");
        playButton.setIcon(icon);
        playButton.setBackground(Color.black);
        playButton.setBorder(null);
        
        
        ImageIcon settingsIcon = new ImageIcon("Graphics/settings.png");
        settingsButton = new JButton(settingsIcon);
        settingsButton.setBackground(Color.black);
        settingsButton.setBorder(null);

        buttonPanel.add(playButton);
        buttonPanel.add(settingsButton);

        add(buttonPanel, BorderLayout.CENTER);
        setBackground(Color.darkGray);
        validate();
    }

}
