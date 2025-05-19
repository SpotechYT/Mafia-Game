import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SettingPanel extends JPanel {

    public JButton backButton;

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
    }
}
