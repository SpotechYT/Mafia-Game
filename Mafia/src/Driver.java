import javax.swing.JFrame;
import javax.swing.JLabel;

public class Driver {
    public static void main(String[] args)
    {
        // Create a new JFrame
        JFrame frame = new JFrame("Mafia");

        // Create a label
        JLabel label = new JLabel("The Mafia Game", JLabel.CENTER);

        // Add the label to the frame
        frame.add(label);

        // Set frame properties
        frame.setSize(1280, 720);

        // Close when X clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Render the frame
        frame.setVisible(true);
    }
}