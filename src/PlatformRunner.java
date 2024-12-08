import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PlatformRunner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainFrame();
            }
        });
    }

    public static void mainFrame() {
        JFrame jf = new JFrame("Twitter 2.0");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setSize(screenSize.width, screenSize.height);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setLayout(new BorderLayout());

        Welcome.welcomeGUI(jf);

        jf.setVisible(true);
    }
}
