import java.awt.*;
import javax.swing.*;


public class SettingPanel extends JPanel {

    public JButton backButton;
    public JButton nameButton;
    public JPanel centerPanel;
    public JPanel rigthPanel;

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
        centerPanel = new JPanel(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel(new BorderLayout());
        nameButton = new JButton("Change Name");
        leftPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
        leftPanel.add(new JScrollPane(nameButton), BorderLayout.NORTH);

        centerPanel.add(leftPanel);

        rigthPanel = new JPanel(new BorderLayout());
        JTextArea defaultTextArea = new JTextArea("Click on a setting");
        rigthPanel.add(defaultTextArea, BorderLayout.CENTER);
        centerPanel.add(rigthPanel);

        add(centerPanel, BorderLayout.CENTER);
        
    }

    public void onChangeName(){
        nameButton.addActionListener(e -> {
            rigthPanel = new JPanel(new BorderLayout());
            JButton newName = new JButton("Change");
            rigthPanel.add(new JScrollPane(newName), BorderLayout.CENTER);
            centerPanel.add(rigthPanel);
        });
    }

    public void resetSettings(){
        rigthPanel = new JPanel(new BorderLayout());
        JTextArea defaultTextArea = new JTextArea("Click on a setting");
        rigthPanel.add(defaultTextArea, BorderLayout.CENTER);
        centerPanel.add(rigthPanel);
    }
}
