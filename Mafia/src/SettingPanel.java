import java.awt.*;
import javax.swing.*;


public class SettingPanel extends JPanel {

    public JButton backButton;
    public JButton nameButton;
    public JPanel centerPanel;
    public JPanel rightPanel;

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

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Click on a setting"), BorderLayout.NORTH);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        
        nameButton.addActionListener(e -> {
            rightPanel.removeAll();
            JTextField nameTextPane = new JTextField("Enter your name");
            nameTextPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            rightPanel.add(nameTextPane, BorderLayout.NORTH);
            JButton newName = new JButton("Change");
            newName.addActionListener(ev -> {
                String name = nameTextPane.getText();
                //code to change the name
                resetSettings();
            });
            rightPanel.add(new JScrollPane(newName), BorderLayout.CENTER);
            rightPanel.revalidate();
            rightPanel.repaint();

        });
        
    }

    public void resetSettings(){
        rightPanel.removeAll();
        rightPanel.add(new JLabel("Click on a setting"), BorderLayout.NORTH);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
}
