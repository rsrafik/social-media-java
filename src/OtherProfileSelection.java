import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class OtherProfileSelection {

    public static void mainView(JPanel mainPanel) throws IOException, ClassNotFoundException {
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
        JLabel usernameLabel = new JLabel(SearchTextField.chosenOne.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(usernameLabel);

        // Add spacing between user info and buttons
        topPanel.add(Box.createVerticalStrut(20));

        // Following button
        JButton followingButton = new JButton("Follow");
        followingButton.setFont(new Font("Arial", Font.PLAIN, 14));
        followingButton.setFocusPainted(false);
        followingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followingButton.setPreferredSize(new Dimension(150, 40));
        followingButton.addActionListener(e -> {
            boolean sendFollow;

            try {
                sendFollow = PlatformRunner.client.sendFollowRequest(SearchTextField.chosenOne.getId());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            if (sendFollow)
                JOptionPane.showMessageDialog(null, "Sent follow request!!");
        });

        topPanel.add(followingButton);

        // Blocked button
        JButton blockedButton = new JButton("Block");
        blockedButton.setFont(new Font("Arial", Font.PLAIN, 14));
        blockedButton.setFocusPainted(false);
        blockedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        blockedButton.setPreferredSize(new Dimension(150, 40));
        blockedButton.addActionListener(e -> {
            boolean blockSuccess;

            try {
                blockSuccess = PlatformRunner.client.blockUser(SearchTextField.chosenOne.getId());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            if (blockSuccess)
                JOptionPane.showMessageDialog(null, "Blocked user!!");
        });

        topPanel.add(blockedButton);

        // Add the top panel to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Add the single scrollable content for posts
        JScrollPane postScrollPane = createPostsScrollPane();
        mainPanel.add(postScrollPane, BorderLayout.CENTER);
    }

    private static JScrollPane createPostsScrollPane() throws IOException, ClassNotFoundException {
        // Panel to hold post content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        List<Integer> postIds = SearchTextField.chosenOne.getPostIds();
        List<Post> posts = new ArrayList<>();

        for(Integer postId : postIds)
            posts.add(PlatformRunner.client.fetchPost(postId));

        // Add 5 individual posts
        for (int i = 0; i < postIds.size(); i++) {
            JPanel post = SinglePost.individualPost(posts.get(i), "friend");
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
