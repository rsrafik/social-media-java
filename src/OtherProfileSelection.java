import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class OtherProfileSelection {

    public static void mainView(JPanel mainPanel) {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Create the top panel with a fixed height of 185 pixels
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS)); // Vertical layout
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(mainPanel.getWidth(), 175));
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
//
//        topPanel.add(Box.createVerticalStrut(20));

        // Username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(usernameLabel);

        // User ID label
        JLabel userIDLabel = new JLabel("User ID: xxx");
        userIDLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userIDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(userIDLabel);

        // Add spacing between user info and buttons
        topPanel.add(Box.createVerticalStrut(20));

        // Following button
        JButton followingButton = new JButton("Follow");
        followingButton.setFont(new Font("Arial", Font.PLAIN, 14));
        followingButton.setFocusPainted(false);
        followingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followingButton.setPreferredSize(new Dimension(150, 40));
        topPanel.add(followingButton);

        // Blocked button
        JButton blockedButton = new JButton("Block");
        blockedButton.setFont(new Font("Arial", Font.PLAIN, 14));
        blockedButton.setFocusPainted(false);
        blockedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        blockedButton.setPreferredSize(new Dimension(150, 40));
        topPanel.add(blockedButton);

        // Add the top panel to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Add the single scrollable content for posts
        JScrollPane postScrollPane = createPostsScrollPane();
        mainPanel.add(postScrollPane, BorderLayout.CENTER);
    }

    private static JScrollPane createPostsScrollPane() {
        // Panel to hold post content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Add 5 individual posts
        for (int i = 1; i <= 5; i++) {
            JPanel post = SinglePost.individualPost("friend");
            contentPanel.add(post);
            contentPanel.add(Box.createVerticalStrut(40)); // Add spacing between posts
        }

        // Create a scroll pane for the content panel
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        scrollPane.setBorder(null); // Remove border for cleaner look

        return scrollPane;
    }
}
