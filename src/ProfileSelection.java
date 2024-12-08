import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ProfileSelection {

    private static JScrollPane currentScrollPane; // Keeps track of the current scroll pane
    private static User currentUser;
    private static JPanel mainPanelRef; // Store a reference to the main panel for refreshing

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
        mainPanelRef = mainPanel; // Store the reference to mainPanel
        mainPanel.removeAll(); // Clear any existing content
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        currentUser = PlatformRunner.client.fetchLoggedInUser();

        // Create the top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(mainPanel.getWidth(), 195));
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        topPanel.add(Box.createVerticalStrut(20));

        // Username label
        JLabel usernameLabel = new JLabel(currentUser.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(usernameLabel);

        topPanel.add(Box.createVerticalStrut(10));

        // Posts button
        JButton postsButton = new JButton("Posts");
        postsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        postsButton.setFocusPainted(false);
        postsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        postsButton.setPreferredSize(new Dimension(150, 40));
        topPanel.add(postsButton);

        topPanel.add(Box.createVerticalStrut(10));

        // Following button
        JButton followingButton = new JButton("Following");
        followingButton.setFont(new Font("Arial", Font.PLAIN, 14));
        followingButton.setFocusPainted(false);
        followingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followingButton.setPreferredSize(new Dimension(150, 40));
        topPanel.add(followingButton);

        topPanel.add(Box.createVerticalStrut(10));

        // Blocked button
        JButton blockedButton = new JButton("Blocked");
        blockedButton.setFont(new Font("Arial", Font.PLAIN, 14));
        blockedButton.setFocusPainted(false);
        blockedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        blockedButton.setPreferredSize(new Dimension(150, 40));
        topPanel.add(blockedButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Initial scroll content
        currentScrollPane = createPostsScrollPane();
        mainPanel.add(currentScrollPane, BorderLayout.CENTER);

        followingButton.addActionListener(e -> {
            try {
                switchToFollowingScrollPane(mainPanel);
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        postsButton.addActionListener(e -> {
            try {
                switchToPostsScrollPane(mainPanel);
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        blockedButton.addActionListener(e -> {
            try {
                switchToBlockedScrollPane(mainPanel);
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private static JScrollPane createPostsScrollPane() throws IOException, ClassNotFoundException {
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

    private static JScrollPane createFollowingScrollPane() throws IOException, ClassNotFoundException {
        // Re-fetch user here to ensure list is updated
        currentUser = PlatformRunner.client.fetchLoggedInUser();

        JPanel followingPanel = new JPanel();
        followingPanel.setLayout(new BoxLayout(followingPanel, BoxLayout.Y_AXIS));
        followingPanel.setBackground(Color.WHITE);

        List<Integer> followingIds = currentUser.getFollowingIds();
        List<UserInfo> followingUsers = new ArrayList<>();

        for (Integer followingId : followingIds)
            followingUsers.add(PlatformRunner.client.fetchUserInfo(followingId));

        for (int i = 0; i < followingUsers.size(); i++) {
            followingPanel.add(createFollowingRow(followingUsers.get(i)));
            if (i < 25) {
                followingPanel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(followingPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        return scrollPane;
    }

    private static JPanel createFollowingRow(UserInfo userInfo) {
        JPanel rowPanel = new JPanel(new GridBagLayout());
        rowPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        rowPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 5, 0, 5);

        JLabel usernameLabel = new JLabel(userInfo.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        rowPanel.add(usernameLabel, gbc);

        JButton unfollowButton = new JButton("Unfollow");
        unfollowButton.setFont(new Font("Arial", Font.PLAIN, 14));
        unfollowButton.setFocusPainted(false);
        gbc.gridx = 1; gbc.gridy = 0;
        rowPanel.add(unfollowButton, gbc);

        unfollowButton.addActionListener(e -> {
            try {
                boolean unfollowSuccess = PlatformRunner.client.unfollowUser(userInfo.getId());
                if (unfollowSuccess) {
                    JOptionPane.showMessageDialog(null, "Unfollowed successfully!");
                    refreshFollowingList();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return rowPanel;
    }

    private static JScrollPane createBlockedScrollPane() throws IOException, ClassNotFoundException {
        // Re-fetch user here as well
        currentUser = PlatformRunner.client.fetchLoggedInUser();

        JPanel blockedPanel = new JPanel();
        blockedPanel.setLayout(new BoxLayout(blockedPanel, BoxLayout.Y_AXIS));
        blockedPanel.setBackground(Color.WHITE);

        List<Integer> blockedIds = currentUser.getBlockedUserIds();
        List<UserInfo> blockedUsers = new ArrayList<>();

        for (Integer blockedId : blockedIds)
            blockedUsers.add(PlatformRunner.client.fetchUserInfo(blockedId));

        for (int i = 0; i < blockedUsers.size(); i++) {
            blockedPanel.add(createBlockedRow(blockedUsers.get(i)));
            if (i < 25) {
                blockedPanel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(blockedPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        return scrollPane;
    }

    private static JPanel createBlockedRow(UserInfo userInfo) {
        JPanel rowPanel = new JPanel(new GridBagLayout());
        rowPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        rowPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 5, 0, 5);

        JLabel usernameLabel = new JLabel(userInfo.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        rowPanel.add(usernameLabel, gbc);

        JButton unBlockButton = new JButton("Unblock");
        unBlockButton.setFont(new Font("Arial", Font.PLAIN, 14));
        unBlockButton.setFocusPainted(false);
        gbc.gridx = 1; gbc.gridy = 0;
        rowPanel.add(unBlockButton, gbc);

        unBlockButton.addActionListener(e -> {
            try {
                boolean unBlockSuccess = PlatformRunner.client.unblockUser(userInfo.getId());
                if (unBlockSuccess) {
                    JOptionPane.showMessageDialog(null, "Unblocked successfully!");
                    refreshBlockedList();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return rowPanel;
    }

    private static void switchToFollowingScrollPane(JPanel mainPanel) throws IOException, ClassNotFoundException {
        currentUser = PlatformRunner.client.fetchLoggedInUser();
        mainPanel.remove(currentScrollPane);
        currentScrollPane = createFollowingScrollPane();
        mainPanel.add(currentScrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private static void switchToPostsScrollPane(JPanel mainPanel) throws IOException, ClassNotFoundException {
        currentUser = PlatformRunner.client.fetchLoggedInUser();
        mainPanel.remove(currentScrollPane);
        currentScrollPane = createPostsScrollPane();
        mainPanel.add(currentScrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private static void switchToBlockedScrollPane(JPanel mainPanel) throws IOException, ClassNotFoundException {
        currentUser = PlatformRunner.client.fetchLoggedInUser();
        mainPanel.remove(currentScrollPane);
        currentScrollPane = createBlockedScrollPane();
        mainPanel.add(currentScrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Refresh methods: fully rebuild the UI and then switch to the desired pane
    private static void refreshFollowingList() {
        try {
            mainPanelRef.removeAll();
            mainView(mainPanelRef);
            switchToFollowingScrollPane(mainPanelRef);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        mainPanelRef.revalidate();
        mainPanelRef.repaint();
    }

    private static void refreshBlockedList() {
        try {
            mainPanelRef.removeAll();
            mainView(mainPanelRef);
            switchToBlockedScrollPane(mainPanelRef);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        mainPanelRef.revalidate();
        mainPanelRef.repaint();
    }
}
