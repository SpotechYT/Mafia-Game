import java.awt.*;
import javax.swing.*;


public class SettingPanel extends JPanel {

    public JButton backButton;
    public JButton nameButton;

    public SettingPanel() {
        // Set layout
        setLayout(new BorderLayout());

        // Create a top panel for the back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Back");

        // Add back button to the top panel
        topPanel.add(backButton);

        // Add top panel to the top (NORTH) of the main panel
        add(topPanel, BorderLayout.NORTH);
        // You can add game content to other parts of the panel later
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel(new BorderLayout());
        nameButton = new JButton("Change Name");
        leftPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
        leftPanel.add(new JScrollPane(nameButton), BorderLayout.NORTH);

        centerPanel.add(leftPanel);

        add(centerPanel, BorderLayout.CENTER);
        
    }
}
