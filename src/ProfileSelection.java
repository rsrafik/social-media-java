import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ProfileSelection {

    private static JScrollPane currentScrollPane; // Keeps track of the current scroll pane
    private static User currentUser;

    static {
        try {
            currentUser = PlatformRunner.client.fetchLoggedInUser();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void mainView(JPanel mainPanel) throws IOException, ClassNotFoundException {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Create the top panel with a fixed height of 185 pixels
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS)); // Vertical layout
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(mainPanel.getWidth(), 185));
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        topPanel.add(Box.createVerticalStrut(20));

        // Username label
        JLabel usernameLabel = new JLabel(currentUser.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(usernameLabel);

        // Add spacing between user info and buttons
        topPanel.add(Box.createVerticalStrut(20));

        // Following button
        JButton postsButton = new JButton("Posts");
        postsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        postsButton.setFocusPainted(false);
        postsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        postsButton.setPreferredSize(new Dimension(150, 40));
        topPanel.add(postsButton);

        // Following button
        JButton followingButton = new JButton("Following");
        followingButton.setFont(new Font("Arial", Font.PLAIN, 14));
        followingButton.setFocusPainted(false);
        followingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followingButton.setPreferredSize(new Dimension(150, 40));
        topPanel.add(followingButton);

        // Blocked button
        JButton blockedButton = new JButton("Blocked");
        blockedButton.setFont(new Font("Arial", Font.PLAIN, 14));
        blockedButton.setFocusPainted(false);
        blockedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        blockedButton.setPreferredSize(new Dimension(150, 40));
        topPanel.add(blockedButton);

        // Add the top panel to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Add the initial scrollable content
        currentScrollPane = createPostsScrollPane();
        mainPanel.add(currentScrollPane, BorderLayout.CENTER);

        // Add action listener to Following button
        followingButton.addActionListener(e -> switchToFollowingScrollPane(mainPanel));

        // Add action listener to Blocked button
        postsButton.addActionListener(e -> {
            try {
                switchToPostsScrollPane(mainPanel);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        blockedButton.addActionListener(e -> switchToBlockedScrollPane(mainPanel));
    }

    private static JScrollPane createPostsScrollPane() throws IOException, ClassNotFoundException {
        // Re-fetch the current user to ensure the posts are up to date
        currentUser = PlatformRunner.client.fetchLoggedInUser();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        List<Integer> postIds = currentUser.getPostIds();
        List<Post> posts = new ArrayList<>();

        for(int postId : postIds) {
            posts.add(PlatformRunner.client.fetchPost(postId));
        }

        for (Post post : posts) {
            JPanel postPanel = SinglePost.individualPost(post, "user");
            contentPanel.add(postPanel);
            contentPanel.add(Box.createVerticalStrut(40));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        return scrollPane;
    }


    private static JScrollPane createFollowingScrollPane() {
        JPanel followingPanel = new JPanel();
        followingPanel.setLayout(new BoxLayout(followingPanel, BoxLayout.Y_AXIS));
        followingPanel.setBackground(Color.WHITE);

        for (int i = 1; i <= 25; i++) {
            followingPanel.add(createFollowingRow("user" + i)); // Dynamically create usernames like "user1", "user2", etc.
            if (i < 25) {
                followingPanel.add(Box.createVerticalStrut(15)); // Add spacing between rows, but not after the last one
            }
        }


        JScrollPane scrollPane = new JScrollPane(followingPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        return scrollPane;
    }


    private static JPanel createFollowingRow(String username) {
        JPanel rowPanel = new JPanel(new GridBagLayout());
        rowPanel.setBorder(new EmptyBorder(5, 10, 5, 10)); // Minimal padding
        rowPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE; // Components retain their preferred sizes
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        gbc.insets = new Insets(0, 5, 0, 5); // Minimal spacing between components

        // Username label
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; // First column
        gbc.gridy = 0; // First row
        rowPanel.add(usernameLabel, gbc);

        // Unfollow button
        JButton unfollowButton = new JButton("Unfollow");
        unfollowButton.setFont(new Font("Arial", Font.PLAIN, 14));
        unfollowButton.setFocusPainted(false);
        gbc.gridx = 1; // Second column, same row
        gbc.gridy = 0;
        rowPanel.add(unfollowButton, gbc);

        return rowPanel;
    }



    private static void switchToFollowingScrollPane(JPanel mainPanel) {
        mainPanel.remove(currentScrollPane); // Remove the existing scrollable content
        currentScrollPane = createFollowingScrollPane(); // Create new scrollable content
        mainPanel.add(currentScrollPane, BorderLayout.CENTER); // Add the new content
        mainPanel.revalidate(); // Refresh the layout
        mainPanel.repaint();
    }

    private static void switchToPostsScrollPane(JPanel mainPanel) throws IOException, ClassNotFoundException {
        // Re-fetch the current user here
        currentUser = PlatformRunner.client.fetchLoggedInUser();

        mainPanel.remove(currentScrollPane);
        currentScrollPane = createPostsScrollPane();
        mainPanel.add(currentScrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private static JScrollPane createBlockedScrollPane() {
        JPanel followingPanel = new JPanel();
        followingPanel.setLayout(new BoxLayout(followingPanel, BoxLayout.Y_AXIS));
        followingPanel.setBackground(Color.WHITE);

        for (int i = 1; i <= 25; i++) {
            followingPanel.add(createBlockedRow("user" + i)); // Dynamically create usernames like "user1", "user2", etc.
            if (i < 25) {
                followingPanel.add(Box.createVerticalStrut(15)); // Add spacing between rows, but not after the last one
            }
        }


        JScrollPane scrollPane = new JScrollPane(followingPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        return scrollPane;
    }


    private static JPanel createBlockedRow(String username) {
        JPanel rowPanel = new JPanel(new GridBagLayout());
        rowPanel.setBorder(new EmptyBorder(5, 10, 5, 10)); // Minimal padding
        rowPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE; // Components retain their preferred sizes
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        gbc.insets = new Insets(0, 5, 0, 5); // Minimal spacing between components

        // Username label
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; // First column
        gbc.gridy = 0; // First row
        rowPanel.add(usernameLabel, gbc);

        // Unfollow button
        JButton unfollowButton = new JButton("Unblock");
        unfollowButton.setFont(new Font("Arial", Font.PLAIN, 14));
        unfollowButton.setFocusPainted(false);
        gbc.gridx = 1; // Second column, same row
        gbc.gridy = 0;
        rowPanel.add(unfollowButton, gbc);

        return rowPanel;
    }



    private static void switchToBlockedScrollPane(JPanel mainPanel) {
        mainPanel.remove(currentScrollPane); // Remove the existing scrollable content
        currentScrollPane = createBlockedScrollPane(); // Create new scrollable content
        mainPanel.add(currentScrollPane, BorderLayout.CENTER); // Add the new content
        mainPanel.revalidate(); // Refresh the layout
        mainPanel.repaint();
    }
}
