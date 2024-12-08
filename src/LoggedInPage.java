import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoggedInPage {
    private static String selectedButton = "Home"; // Default selected button
    private static TransparentJButton selectedButtonReference;
    private static JPanel mainPanel; // Panel for dynamic content

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Logged In Page");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            testFrame.setSize(screenSize.width, screenSize.height);
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setLayout(new BorderLayout());

            // Load the GUI
            totalGUI(testFrame);

            testFrame.setVisible(true);
        });
    }

    public static void totalGUI(JFrame jf) {
        jf.getContentPane().removeAll();
        // Add the side panel
        sidePanel(jf);

        // Initialize the main panel for dynamic content
        mainPanel = new JPanel(new BorderLayout());
        jf.add(mainPanel, BorderLayout.CENTER);

        // Display the default "Home" view
        updateView("Home", jf);

        // Refresh the frame
        jf.revalidate();
        jf.repaint();
    }

    public static void sidePanel(JFrame jf) {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBackground(Color.WHITE);

        // Set fixed width
        sidePanel.setPreferredSize(new Dimension(200, jf.getHeight()));
        jf.add(sidePanel, BorderLayout.WEST);

        // Create a container for the buttons
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setBackground(Color.LIGHT_GRAY);

        buttonContainer.add(Box.createVerticalGlue());

        // Create the grid panel
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(4, 1, 0, 0));
        gridPanel.setBackground(Color.LIGHT_GRAY);

        // Array to hold all buttons
        TransparentJButton[] buttons = new TransparentJButton[4];

        // Create buttons and add them
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
            // Display the confirmation dialog
            int result = JOptionPane.showConfirmDialog(
                    jf,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            // Handle the user's choice
            if (result == JOptionPane.YES_OPTION) {
                // Load the Welcome page instead of disposing of the frame
                jf.getContentPane().removeAll();
                Welcome.welcomeGUI(jf);
                jf.revalidate();
                jf.repaint();
            }

            selectedButton = "Log Out"; // Retain functionality to update the selected button
        });

        gridPanel.add(buttons[3]);

        buttonContainer.add(gridPanel);
        buttonContainer.add(Box.createVerticalGlue());
        sidePanel.add(buttonContainer, BorderLayout.CENTER);
    }

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
                updateView(text, jf);
            }
        });

        gridPanel.add(button);
        return button;
    }

    private static void resetButtons(TransparentJButton[] buttons) {
        for (TransparentJButton button : buttons) {
            if (button != null) {
                button.setBackground(Color.LIGHT_GRAY);
                button.setForeground(Color.BLACK);
            }
        }
    }

    private static void updateView(String view, JFrame jf) {
        mainPanel.removeAll(); // Clear the current view

        switch (view) {
            case "Home" -> HomeSelection.mainView(mainPanel);
            case "Profile" -> ProfileSelection.mainView(mainPanel);
            case "Inbox" -> InboxSelection.mainView(mainPanel);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
