import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * OtherProfileSelection
 *
 * This class displays another user's profile, allowing the current user to follow or block them.
 * It also shows the selected user's posts in a scrollable panel.
 *
 * @author Rachel Rafik, L22
 *
 * @version December 8, 2024
 */
class OtherProfileSelection {

    /**
     * Sets up and displays the other user's profile view, including options to follow or block,
     * as well as their posts in a scrollable panel.
     *
     * @param mainPanel the main panel to which components will be added
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if a required class cannot be found
     */
    public static void mainView(JPanel mainPanel) throws IOException, ClassNotFoundException {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(mainPanel.getWidth(), 175));
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel(SearchTextField.chosenOne.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(usernameLabel);
        topPanel.add(Box.createVerticalStrut(20));

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

        topPanel.add(Box.createVerticalStrut(10));

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

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JScrollPane postScrollPane = createPostsScrollPane();
        mainPanel.add(postScrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates and returns a scroll pane containing the selected user's posts.
     *
     * @return a JScrollPane containing the selected user's posts
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if a required class cannot be found
     */
    private static JScrollPane createPostsScrollPane() throws IOException, ClassNotFoundException {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        List<Integer> postIds = SearchTextField.chosenOne.getPostIds();
        List<Post> posts = new ArrayList<>();

        for (Integer postId : postIds)
            posts.add(PlatformRunner.client.fetchPost(postId));

        for (int i = 0; i < postIds.size(); i++) {
            JPanel post = SinglePost.individualPost(posts.get(i), "friend");
            contentPanel.add(post);
            contentPanel.add(Box.createVerticalStrut(40));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        return scrollPane;
    }
}
