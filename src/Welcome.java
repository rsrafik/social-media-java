import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Welcome
 *
 * This class displays the initial welcome interface.
 * It provides options for the user to log in or sign up,
 * and transitions to appropriate pages based on user actions.
 *
 * @author Rachel Rafik, L22
 * @version December 8, 2024
 */
public class Welcome {

    /**
     * Displays the welcome GUI on the given JFrame.
     *
     * @param jf the main JFrame on which to display the welcome GUI
     */
    public static void welcomeGUI(JFrame jf) {
        jf.add(centerLayout(jf), BorderLayout.CENTER);
    }

    /**
     * Creates and returns the main center layout panel containing the log in,
     * sign up, and exit options.
     *
     * @param jf the main JFrame
     * @return a JPanel containing the center layout
     */
    private static JPanel centerLayout(JFrame jf) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton loginButton = new JButton("Log In");
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginPage(jf);
            }
        });

        JLabel or = new JLabel("-----or-----");
        or.setAlignmentX(Component.CENTER_ALIGNMENT);
        or.setBorder(new EmptyBorder(5,0,5,0));

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFocusPainted(false);
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSignUpPage(jf);
            }
        });

        TransparentJButton exitButton = new TransparentJButton("EXIT");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(Box.createVerticalGlue());
        panel.add(loginButton);
        panel.add(or);
        panel.add(signUpButton);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(exitButton);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Displays the login page, replacing the current content in the main frame.
     *
     * @param jf the main JFrame
     */
    private static void showLoginPage(JFrame jf) {
        jf.getContentPane().removeAll();

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());

        TransparentJButton backButton = new TransparentJButton("< Back");
        backButton.setBorder(new EmptyBorder(10,10,0,0));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.getContentPane().removeAll();
                jf.add(centerLayout(jf), BorderLayout.CENTER);
                jf.revalidate();
                jf.repaint();
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        loginPanel.add(topPanel, BorderLayout.NORTH);

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
        submitButton.setFocusPainted(false);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                boolean loginSuccess;

                try {
                    loginSuccess = PlatformRunner.client.logIn(username,password);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (loginSuccess) {
                    try {
                        LoggedInPage.totalGUI(jf);
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Incorrect username or password.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
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

        loginPanel.add(formPanel, BorderLayout.CENTER);

        jf.add(loginPanel, BorderLayout.CENTER);

        jf.revalidate();
        jf.repaint();
    }

    /**
     * Displays the sign-up page, replacing the current content in the main frame.
     *
     * @param jf the main JFrame
     */
    private static void showSignUpPage(JFrame jf) {
        jf.getContentPane().removeAll();

        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(new BorderLayout());

        TransparentJButton backButton = new TransparentJButton("< Back");
        backButton.setBorder(new EmptyBorder(10, 10, 0, 0));
        backButton.addActionListener(e -> {
            jf.getContentPane().removeAll();
            jf.add(centerLayout(jf), BorderLayout.CENTER);
            jf.revalidate();
            jf.repaint();
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        signUpPanel.add(topPanel, BorderLayout.NORTH);

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

        JLabel errorLabel = new JLabel("ERROR: Password too short!");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);

        JButton submitButton = new JButton("Sign Up");
        submitButton.setFocusPainted(false);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (password.length() < 8) {
                errorLabel.setVisible(true);
            } else {
                errorLabel.setVisible(false);

                boolean signUpSuccess;

                try {
                    signUpSuccess = PlatformRunner.client.createUser(username,password);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (signUpSuccess) {
                    boolean loginSuccess;
                    try {
                        loginSuccess = PlatformRunner.client.logIn(username,password);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    if (loginSuccess) {
                        try {
                            LoggedInPage.totalGUI(jf);
                        } catch (IOException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Error signing in",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Username already exists",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        formPanel.add(Box.createVerticalGlue());
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(errorLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(submitButton);
        formPanel.add(Box.createVerticalGlue());

        signUpPanel.add(formPanel, BorderLayout.CENTER);

        jf.add(signUpPanel, BorderLayout.CENTER);

        jf.revalidate();
        jf.repaint();
    }
}
