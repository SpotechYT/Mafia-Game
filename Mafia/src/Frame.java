
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener {

    //Font for score
    Font myFont = new Font("Courier", Font.BOLD, 25);

    //frame width/height
    int width = 1280;
    int height = 720;

    //Function to get image (used for background)
    // private Image getImage(String path) {
    //     Image tempImage = null;
    //     try {
    //         tempImage = Toolkit.getDefaultToolkit().getImage(path);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return tempImage;
    // }

    public void paint(Graphics g) {
        super.paintComponent(g);

        // Add the background first thing so it's behind everything
        //Image bkgnd = getImage("/imgs/Background.png");
        //g.drawImage(bkgnd, 0, 0, 1010, 539, null);

        g.setFont(myFont);
        g.setColor(Color.white);
        g.drawString("Mafia Game", 5, 503);

    }

    public static void main(String[] arg) {
        Frame f = new Frame();
    }

    public Frame() {
        //Set window title
        JFrame f = new JFrame("Mafia Game");

        //Set Favicon
        //f.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imgs/Duck1.png")));

        f.setSize(new Dimension(width, height));
        f.setBackground(Color.black);
        f.add(this);
        f.setResizable(false);
        f.addMouseListener(this);
        f.addKeyListener(this);


        //the cursor image must be outside of the src folder
        //you will need to import a couple of classes to make it fully 
        //functional! use eclipse quick-fixes
        //setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("DuckCursor.png").getImage(),new Point(0, 0), "duck cursor"));

        Timer t = new Timer(16, this);
        t.start();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent m) {

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println("Key Pressed: " + arg0.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

}
