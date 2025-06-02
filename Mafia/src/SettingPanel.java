import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

public class SettingPanel extends JPanel {

    public JButton backButton;
    public JButton nameButton;
    public JPanel centerPanel;
    public JPanel rightPanel;
    public JButton generateButton;

    public SettingPanel() throws Exception {
        // Set layout
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Create a top panel for the back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.BLACK);
        backButton = new JButton("Back");

        // Add back button to the top panel
        topPanel.add(backButton);

        // Add top panel to the top (NORTH) of the main panel
        add(topPanel, BorderLayout.NORTH);
        // You can add game content to other parts of the panel later
        centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.setBackground(Color.BLACK);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        nameButton = new JButton("Change Name");
        generateButton = new JButton("Generate Scenario");
        TitledBorder border = BorderFactory.createTitledBorder("Settings");
        border.setTitleColor(Color.WHITE);
        leftPanel.setBorder(border);
        leftPanel.add(new JScrollPane(nameButton));
        leftPanel.add(new JScrollPane(generateButton));

        centerPanel.add(leftPanel);

        rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Click on a setting");
        label.setForeground(Color.WHITE);
        rightPanel.add(label, BorderLayout.NORTH);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        
        nameButton.addActionListener(e -> {
            rightPanel.removeAll();
            JTextField nameTextPane = new JTextField(Driver.getPlayerName());
            nameTextPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 30));
            nameTextPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            nameTextPane.setMinimumSize(new Dimension(Integer.MAX_VALUE, 30));
            rightPanel.add(nameTextPane);
            JButton newName = new JButton("Change");
            newName.addActionListener(ev -> {
                String name = nameTextPane.getText();
                Driver.setPlayerName(name);
                //code to change the name
                resetSettings();
            });
            newName.setPreferredSize(new Dimension(100, 50));
            newName.setMaximumSize(new Dimension(100, 50));
            newName.setMinimumSize(new Dimension(100, 50));
            newName.setAlignmentX(Component.CENTER_ALIGNMENT);
            rightPanel.add(newName);
            rightPanel.revalidate();
            rightPanel.repaint();

        });
        
        generateButton.addActionListener(e -> {
            rightPanel.removeAll();

            // Show a loading message while generating
            JTextArea scenarioTextArea = new JTextArea("Generating scenario...");
            scenarioTextArea.setLineWrap(true);
            scenarioTextArea.setWrapStyleWord(true);
            scenarioTextArea.setPreferredSize(new Dimension(Integer.MAX_VALUE, 300));
            scenarioTextArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
            scenarioTextArea.setMinimumSize(new Dimension(Integer.MAX_VALUE, 300));
            rightPanel.add(scenarioTextArea);

            JButton generate = new JButton("Back");
            generate.addActionListener(ev -> {
                resetSettings();
            });
            generate.setPreferredSize(new Dimension(100, 50));
            generate.setMaximumSize(new Dimension(100, 50));
            generate.setMinimumSize(new Dimension(100, 50));
            generate.setAlignmentX(Component.CENTER_ALIGNMENT);
            rightPanel.add(generate);

            rightPanel.revalidate();
            rightPanel.repaint();

            // Run scenario generation in the background
            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return MafiaScenarioGenerator.getScenario(Driver.getPlayerName(), false);
                }

                @Override
                protected void done() {
                    try {
                        String scenario = get();
                        scenarioTextArea.setText(scenario != null ? scenario : "Failed to generate scenario.");
                        scenarioTextArea.setEditable(false);
                    } catch (Exception ex) {
                        scenarioTextArea.setText("Error: " + ex.getMessage());
                    }
                }
            }.execute();
        });
    }


    public void resetSettings(){
        rightPanel.removeAll();
        rightPanel.add(new JLabel("Click on a setting"), BorderLayout.NORTH);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
}
