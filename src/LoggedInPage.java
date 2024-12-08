import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

/**
 * LoggedInPage
 *
 * This class manages the main logged-in page view of the application.
 * It provides a side panel with navigation buttons ("Home", "Profile", "Inbox", and "Log Out")
 * and a main panel area that displays the content of the selected view.
 * Users can log out, switch between views, and the display updates accordingly.
 *
 * @author Rachel Rafik, L22
 *
 * @version December 8, 2024
 */
public class LoggedInPage {

    private static String selectedButton = "Home"; // Default selected button
    private static TransparentJButton selectedButtonReference; // Reference to the currently selected button
    private static JPanel mainPanel; // Panel for dynamic content

    /**
     * The main method that launches the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Logged In Page");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            testFrame.setSize(screenSize.width, screenSize.height);
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setLayout(new BorderLayout());
            try {
                totalGUI(testFrame);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            testFrame.setVisible(true);
        });
    }

    /**
     * Sets up the entire GUI, including the side panel and the main content panel.
     *
     * @param jf the JFrame to which the GUI elements are added
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if a required class cannot be found
     */
    public static void totalGUI(JFrame jf) throws IOException, ClassNotFoundException {
        jf.getContentPane().removeAll();
        sidePanel(jf);
        mainPanel = new JPanel(new BorderLayout());
        jf.add(mainPanel, BorderLayout.CENTER);
        updateView("Home", jf);
        jf.revalidate();
        jf.repaint();
    }

    /**
     * Creates and adds the side panel with navigation buttons to the given frame.
     *
     * @param jf the JFrame to which the side panel is added
     */
    public static void sidePanel(JFrame jf) {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setPreferredSize(new Dimension(200, jf.getHeight()));
        jf.add(sidePanel, BorderLayout.WEST);

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setBackground(Color.LIGHT_GRAY);
        buttonContainer.add(Box.createVerticalGlue());

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(4, 1, 0, 0));
        gridPanel.setBackground(Color.LIGHT_GRAY);

        TransparentJButton[] buttons = new TransparentJButton[4];
        buttons[0] = createSidebarButton("Home", gridPanel, buttons, jf);
        buttons[1] = createSidebarButton("Profile", gridPanel, buttons, jf);
        buttons[2] = createSidebarButton("Inbox", gridPanel, buttons, jf);

        buttons[3] = new TransparentJButton("Log Out");
        buttons[3].setHorizontalAlignment(SwingConstants.LEFT);
        buttons[3].setPreferredSize(new Dimension(250, 50));
        buttons[3].setFont(new Font("Arial", Font.BOLD, 20));
        buttons[3].setBackground(Color.LIGHT_GRAY);
        buttons[3].setForeground(Color.BLACK);
        buttons[3].setOpaque(true);
        buttons[3].setBorder(new EmptyBorder(0, 20, 0, 0));
        buttons[3].addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    jf,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                boolean loggedoutSuccess;
                try {
                    loggedoutSuccess = PlatformRunner.client.logOut();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (loggedoutSuccess) {
                    jf.getContentPane().removeAll();
                    Welcome.welcomeGUI(jf);
                    jf.revalidate();
                    jf.repaint();
                }
            }
            selectedButton = "Log Out";
        });

        gridPanel.add(buttons[3]);
        buttonContainer.add(gridPanel);
        buttonContainer.add(Box.createVerticalGlue());
        sidePanel.add(buttonContainer, BorderLayout.CENTER);
    }

    /**
     * Creates a sidebar button with the given text and adds it to the specified grid panel.
     *
     * @param text the text to display on the button
     * @param gridPanel the panel to which this button will be added
     * @param buttons an array of all sidebar buttons for resetting their states
     * @param jf the main JFrame
     * @return the created TransparentJButton
     */
    private static TransparentJButton createSidebarButton(String text, JPanel gridPanel, TransparentJButton[] buttons, JFrame jf) {
        TransparentJButton button = new TransparentJButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(0, 20, 0, 0));

        if ("Home".equals(text)) {
            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.WHITE);
            selectedButtonReference = button;
        }

        button.addActionListener(e -> {
            if (!selectedButton.equals(text) || "Home".equals(text)) {
                selectedButton = text;
                resetButtons(buttons);
                button.setBackground(Color.DARK_GRAY);
                button.setForeground(Color.WHITE);
                selectedButtonReference = button;
                try {
                    updateView(text, jf);
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        gridPanel.add(button);
        return button;
    }

    /**
     * Resets all sidebar buttons to their default appearance.
     *
     * @param buttons the array of buttons to reset
     */
    private static void resetButtons(TransparentJButton[] buttons) {
        for (TransparentJButton button : buttons) {
            if (button != null) {
                button.setBackground(Color.LIGHT_GRAY);
                button.setForeground(Color.BLACK);
            }
        }
    }

    /**
     * Updates the mainPanel to display the selected view.
     *
     * @param view the view to display ("Home", "Profile", or "Inbox")
     * @param jf the main JFrame
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if a required class cannot be found
     */
    private static void updateView(String view, JFrame jf) throws IOException, ClassNotFoundException {
        mainPanel.removeAll();
        switch (view) {
            case "Home" -> HomeSelection.mainView(mainPanel);
            case "Profile" -> ProfileSelection.mainView(mainPanel);
            case "Inbox" -> InboxSelection.mainView(mainPanel);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
