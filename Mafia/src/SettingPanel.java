import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;

public class SettingPanel extends JPanel {

    public JButton backButton;
    public JButton nameButton;
    public JPanel centerPanel;
    public JPanel rightPanel;
    public JButton generateButton;
    

    public SettingPanel() throws Exception {
        // Set layout
        setLayout(new BorderLayout());
        // Create a top panel for the back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton();
        ImageIcon backIcon = new ImageIcon("Graphics/back1.png");
        backButton.setIcon(backIcon);
        backButton.setBackground(Color.black);

        // Add back button to the top panel
        topPanel.add(backButton);

        // Add top panel to the top (NORTH) of the main panel
        add(topPanel, BorderLayout.NORTH);
        // You can add game content to other parts of the panel later
        centerPanel = new JPanel(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        nameButton = new JButton();
        ImageIcon icon = new ImageIcon("Graphics/changename2.png");
        nameButton.setIcon(icon);
        nameButton.setBackground(Color.black);
        generateButton = new JButton();
        ImageIcon Icon2 = new ImageIcon("Graphics/senario.png");
        generateButton.setIcon(Icon2);
        generateButton.setBackground(Color.black);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
        leftPanel.add(new JScrollPane(nameButton));
        leftPanel.add(new JScrollPane(generateButton));

        centerPanel.add(leftPanel);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(new JLabel("Click on a setting"), BorderLayout.NORTH);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        
        nameButton.addActionListener(e -> {
            rightPanel.removeAll();
            JTextField nameTextPane = new JTextField(Driver.getPlayerName());
            nameTextPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 30));
            nameTextPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            nameTextPane.setMinimumSize(new Dimension(Integer.MAX_VALUE, 30));
            rightPanel.add(nameTextPane);
            JButton newName = new JButton();
            ImageIcon icon3 = new ImageIcon("Graphics/change.png");
            newName.setIcon(icon3);
            newName.setBackground(Color.black);
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

    //     generateButton.addActionListener(e -> {
    //         rightPanel.removeAll();
    //         try {
    //             JTextArea scenarioTextArea = new JTextArea(MafiaScenarioGenerator.getScenario());
    //             scenarioTextArea.setLineWrap(true);
    //             scenarioTextArea.setWrapStyleWord(true);
    //             scenarioTextArea.setPreferredSize(new Dimension(Integer.MAX_VALUE, 300));
    //             scenarioTextArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
    //             scenarioTextArea.setMinimumSize(new Dimension(Integer.MAX_VALUE, 300));
    //             rightPanel.add(scenarioTextArea);
    //         } catch (Exception e1) {
    //             e1.printStackTrace();
    //         }
    //         JButton generate = new JButton("Back");
    //         generate.addActionListener(ev -> {
    //             resetSettings();
    //         });
    //         generate.setPreferredSize(new Dimension(100, 50));
    //         generate.setMaximumSize(new Dimension(100, 50));
    //         generate.setMinimumSize(new Dimension(100, 50));
    //         generate.setAlignmentX(Component.CENTER_ALIGNMENT);
    //         rightPanel.add(generate);
    //         rightPanel.revalidate();
    //         rightPanel.repaint();

    //     });
        
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

            JButton generate = new JButton();
            ImageIcon icon4 = new ImageIcon("Graphics/back2.png");
            generate.setIcon(icon4);
            generate.setBackground(Color.black);

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
                    return MafiaScenarioGenerator.getScenario(Driver.getPlayer());
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
