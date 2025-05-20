import java.awt.*;
import javax.swing.*;


public class SettingPanel extends JPanel {

    public JButton backButton;
    public JButton nameButton;
    public JTextPane nameTextPane;
    public JButton newName;
    public JPanel centerPanel;
    public JPanel rightPanel;
    public JTextArea defaultTextArea;

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
        defaultTextArea = new JTextArea("Click on a setting");
        rightPanel.add(defaultTextArea, BorderLayout.CENTER);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Add action listener for nameButton here
        
            nameButton.addActionListener(e -> {
                rightPanel.removeAll();
                nameTextPane = new JTextPane();
                rightPanel.add(nameTextPane, BorderLayout.NORTH);
                newName = new JButton("Change");
                newName.addActionListener(ev -> {
                    resetSettings();
                });
                rightPanel.add(new JScrollPane(newName), BorderLayout.CENTER);
                rightPanel.revalidate();
                rightPanel.repaint();

            });
        
    }

    public void resetSettings(){
        rightPanel.removeAll();
        rightPanel.add(defaultTextArea, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
}
