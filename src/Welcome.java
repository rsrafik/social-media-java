import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Welcome {
    public static void welcomeGUI(JFrame jf) {
        // Add the initial center layout
        jf.add(centerLayout(jf), BorderLayout.CENTER);
    }

    private static JPanel centerLayout(JFrame jf) {
        // Create a panel with vertical layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create the "Log In" button
        JButton loginButton = new JButton("Log In");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginPage(jf); // Show login page when clicked

            }
        });

        // Create a "or" icon
        JLabel or = new JLabel("-----or-----");
        or.setAlignmentX(Component.CENTER_ALIGNMENT);
        or.setBorder(new EmptyBorder(5,0,5,0));

        // Create the "Sign Up" button
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSignUpPage(jf);
            }
        });

        // Create the "Exit" button
        TransparentJButton exitButton = new TransparentJButton("EXIT");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
            }
        });

        // Add buttons to the panel
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(Box.createVerticalGlue()); // Add vertical spacing
        panel.add(loginButton);
        panel.add(or); // Add spacing between buttons
        panel.add(signUpButton); // Add vertical spacing
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(exitButton);
        panel.add(Box.createVerticalGlue());


        return panel;
    }

    private static void showLoginPage(JFrame jf) {
        // Clear the current content pane
        jf.getContentPane().removeAll();

        // Create a new panel for the login page
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());

        // Create a "Back" button
        TransparentJButton backButton = new TransparentJButton("< Back");
        backButton.setBorder(new EmptyBorder(10,10,0,0));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go back to the welcome page
                jf.getContentPane().removeAll();
                jf.add(centerLayout(jf), BorderLayout.CENTER);
                jf.revalidate();
                jf.repaint();
            }
        });

        // Add the back button to the top-left corner
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        loginPanel.add(topPanel, BorderLayout.NORTH);

        // Create the login form in the center
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(200, 30));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        PasswordFieldEyeIcon passwordField = new PasswordFieldEyeIcon();
        passwordField.setMaximumSize(new Dimension(200, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton submitButton = new JButton("Log In");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                LoggedInPage.totalGUI(jf); // Load the home page

            }
        });


        formPanel.add(Box.createVerticalGlue());
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(submitButton);
        formPanel.add(Box.createVerticalGlue());

        // Add the form panel to the center
        loginPanel.add(formPanel, BorderLayout.CENTER);

        // Add the login panel to the frame
        jf.add(loginPanel, BorderLayout.CENTER);

        // Refresh the frame
        jf.revalidate();
        jf.repaint();
    }

    private static void showSignUpPage(JFrame jf) {
        // Clear the current content pane
        jf.getContentPane().removeAll();

        // Create a new panel for the sign-up page
        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(new BorderLayout());

        // Create a "Back" button
        TransparentJButton backButton = new TransparentJButton("< Back");
        backButton.setBorder(new EmptyBorder(10, 10, 0, 0));
        backButton.addActionListener(e -> {
            // Go back to the welcome page
            jf.getContentPane().removeAll();
            jf.add(centerLayout(jf), BorderLayout.CENTER);
            jf.revalidate();
            jf.repaint();
        });

        // Add the back button to the top-left corner
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        signUpPanel.add(topPanel, BorderLayout.NORTH);

        // Create the sign-up form in the center
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(200, 30));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Password (at least 8 characters):");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        PasswordFieldEyeIcon passwordField = new PasswordFieldEyeIcon();
        passwordField.setMaximumSize(new Dimension(200, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create the error label (hidden initially)
        JLabel errorLabel = new JLabel("ERROR: Password too short!");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false); // Hide by default

        JButton submitButton = new JButton("Sign Up");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (password.length() < 8) {
                // Show error message if password is too short
                errorLabel.setVisible(true);
            } else {
                // Hide error message and navigate to the home page
                errorLabel.setVisible(false);
                LoggedInPage.totalGUI(jf); // Load the home page
            }
        });


        formPanel.add(Box.createVerticalGlue());
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(errorLabel); // Add error label below password field
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(submitButton);
        formPanel.add(Box.createVerticalGlue());

        // Add the form panel to the center
        signUpPanel.add(formPanel, BorderLayout.CENTER);

        // Add the sign-up panel to the frame
        jf.add(signUpPanel, BorderLayout.CENTER);

        // Refresh the frame
        jf.revalidate();
        jf.repaint();
    }
}
