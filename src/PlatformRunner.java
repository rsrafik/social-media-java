import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * PlatformRunner
 * <p>
 * This class serves as the entry point of the application.
 * It initializes the platform client and launches the main frame.
 *
 * @author Rachel Rafik, L22
 * @version December 8, 2024
 */
public class PlatformRunner {

    /**
     * A static reference to the platform client, connected to the specified host and port.
     */
    public static final PlatformClient client;

    static {
        try {
            client = new PlatformClient("localhost", 5002);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The main method that starts the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> mainFrame());
    }

    /**
     * Initializes and displays the main frame of the application.
     */
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
