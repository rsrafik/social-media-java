import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SinglePost
 *
 * This class creates a UI panel to display a single post, including the
 * username of the creator, the post content, upvote/downvote buttons,
 * and comment actions. Users can also view existing comments in a scrollable
 * dialog. UI updates are performed dynamically after server calls.
 *
 * @author Rachel Rafik, L22
 * @version December 8, 2024
 */
public class SinglePost {

    /**
     * Creates a JPanel representing a single post, including top bar, content,
     * upvote/downvote buttons, and comment actions.
     *
     * @param post The post object containing post data
     * @param role The role of the user viewing this post (e.g., "friend" or "user")
     * @return A JPanel representing the post
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if a required class cannot be found
     */
    public static JPanel individualPost(Post post, String role) throws IOException, ClassNotFoundException {
        User currentUser = PlatformRunner.client.fetchLoggedInUser();
        int currentUserId = currentUser.getId();

        JPanel postPanel = new JPanel();
        postPanel.setBackground(new Color(230, 230, 230));
        postPanel.setLayout(new BorderLayout());

        JPanel userTopper = new JPanel(new BorderLayout());
        userTopper.setPreferredSize(new Dimension(800, 45));
        userTopper.setBackground(new Color(154, 154, 154));

        UserInfo userInfo = PlatformRunner.client.fetchUserInfo(post.getCreatorId());

        JLabel usernameLabel = new JLabel(userInfo.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
        userTopper.add(usernameLabel, BorderLayout.WEST);

        JButton moreButton = new JButton("•••");
        moreButton.setFont(new Font("Arial", Font.BOLD, 20));
        moreButton.setForeground(Color.WHITE);
        moreButton.setFocusPainted(false);
        moreButton.setContentAreaFilled(false);
        moreButton.setBorderPainted(false);
        moreButton.addActionListener(e -> {
            String moreOption = role.equalsIgnoreCase("friend") ? "Hide post?" : "Delete post?";
            int result = JOptionPane.showConfirmDialog(
                    postPanel,
                    moreOption,
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                if (moreOption.equals("Hide post?")) {
                    postPanel.removeAll();
                    postPanel.setBackground(Color.LIGHT_GRAY);
                    postPanel.revalidate();
                    postPanel.repaint();
                }
            }
        });

        userTopper.add(moreButton, BorderLayout.EAST);

        postPanel.add(userTopper, BorderLayout.NORTH);

        JLabel placeholderLabel = new JLabel(
                "<html><div style='text-align: center; padding-top: 15px; padding-bottom: 15px;'>" +
                        post.getContent() + "</div></html>",
                SwingConstants.CENTER
        );
        placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        placeholderLabel.setForeground(new Color(70, 70, 70));

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(new Color(230, 230, 230));
        middlePanel.add(placeholderLabel, BorderLayout.CENTER);

        placeholderLabel.setSize(800, Short.MAX_VALUE);
        Dimension labelSize = placeholderLabel.getPreferredSize();
        int dynamicHeight = 45 + labelSize.height + 30;
        postPanel.setPreferredSize(new Dimension(800, dynamicHeight));
        postPanel.setMaximumSize(new Dimension(800, dynamicHeight));

        postPanel.add(middlePanel, BorderLayout.CENTER);

        JPanel userBottomer = new JPanel(new BorderLayout());
        userBottomer.setPreferredSize(new Dimension(800, 30));
        userBottomer.setBackground(new Color(197, 197, 197));

        JPanel votePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        votePanel.setBackground(new Color(197, 197, 197));

        JLabel upvoteLabel = new JLabel(String.valueOf(post.getUpvoteIds().size()));
        upvoteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JToggleButton upvoteButton = new JToggleButton("▲");
        upvoteButton.setFocusPainted(false);

        JLabel downvoteLabel = new JLabel(String.valueOf(post.getDownvoteIds().size()));
        downvoteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JToggleButton downvoteButton = new JToggleButton("▼");
        downvoteButton.setFocusPainted(false);

        boolean userUpvoted = post.getUpvoteIds().contains(currentUserId);
        boolean userDownvoted = post.getDownvoteIds().contains(currentUserId);
        upvoteButton.setSelected(userUpvoted);
        downvoteButton.setSelected(userDownvoted);

        addVoteButtonListeners(post, upvoteButton, downvoteButton, upvoteLabel, downvoteLabel, currentUserId);

        votePanel.add(upvoteLabel);
        votePanel.add(upvoteButton);
        votePanel.add(Box.createHorizontalStrut(10));
        votePanel.add(downvoteLabel);
        votePanel.add(downvoteButton);

        JPanel commentPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        commentPanel.setBackground(new Color(197, 197, 197));
        JButton makeComment = new JButton("Add Comment");
        makeComment.setFocusPainted(false);
        makeComment.addActionListener(createCommentActionListener(post));
        JButton viewComments = new JButton("View Comments");
        viewComments.setFocusPainted(false);
        viewComments.addActionListener(e -> {
            List<Comment> commentList = null;
            try {
                commentList = PlatformRunner.client.fetchComments(post.getId());
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            List<String> commentMessages = new ArrayList<>();

            if (commentList != null) {
                for (Comment comment : commentList) {
                    try {
                        commentMessages.add(
                                PlatformRunner.client.fetchUserInfo(comment.getCreatorId()).getUsername() + ": "
                                        + comment.getContent()
                        );
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                ScrollableOptionPane.showDialog(null, commentMessages.toArray(new String[0]), "Comments");
            } else {
                ScrollableOptionPane.showDialog(null, new String[0], "Comments");
            }
        });

        commentPanel.add(makeComment);
        commentPanel.add(viewComments);

        userBottomer.add(votePanel, BorderLayout.WEST);
        userBottomer.add(commentPanel, BorderLayout.EAST);

        postPanel.add(userBottomer, BorderLayout.SOUTH);

        return postPanel;
    }

    /**
     * Adds action listeners for the upvote and downvote buttons.
     *
     * @param originalPost the post before voting action
     * @param upvoteButton the upvote toggle button
     * @param downvoteButton the downvote toggle button
     * @param upvoteLabel the label showing number of upvotes
     * @param downvoteLabel the label showing number of downvotes
     * @param currentUserId the current user's ID
     */
    private static void addVoteButtonListeners(
            Post originalPost,
            JToggleButton upvoteButton,
            JToggleButton downvoteButton,
            JLabel upvoteLabel,
            JLabel downvoteLabel,
            int currentUserId
    ) {
        upvoteButton.addActionListener(e -> {
            try {
                PlatformRunner.client.upvotePost(originalPost.getId());
                Post updatedPost = PlatformRunner.client.fetchPost(originalPost.getId());
                refreshPostUI(updatedPost, upvoteButton, downvoteButton, upvoteLabel, downvoteLabel, currentUserId);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        downvoteButton.addActionListener(e -> {
            try {
                PlatformRunner.client.downvotePost(originalPost.getId());
                Post updatedPost = PlatformRunner.client.fetchPost(originalPost.getId());
                refreshPostUI(updatedPost, upvoteButton, downvoteButton, upvoteLabel, downvoteLabel, currentUserId);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Refreshes the UI of a post after an upvote or downvote action.
     *
     * @param updatedPost the updated post after voting
     * @param upvoteButton the upvote toggle button
     * @param downvoteButton the downvote toggle button
     * @param upvoteLabel the label showing number of upvotes
     * @param downvoteLabel the label showing number of downvotes
     * @param currentUserId the current user's ID
     */
    private static void refreshPostUI(
            Post updatedPost,
            JToggleButton upvoteButton,
            JToggleButton downvoteButton,
            JLabel upvoteLabel,
            JLabel downvoteLabel,
            int currentUserId
    ) {
        upvoteLabel.setText(String.valueOf(updatedPost.getUpvoteIds().size()));
        downvoteLabel.setText(String.valueOf(updatedPost.getDownvoteIds().size()));

        boolean userUpvoted = updatedPost.getUpvoteIds().contains(currentUserId);
        boolean userDownvoted = updatedPost.getDownvoteIds().contains(currentUserId);

        upvoteButton.setSelected(userUpvoted);
        downvoteButton.setSelected(userDownvoted);
    }

    /**
     * Creates an action listener for posting a new comment.
     *
     * @param post the post to comment on
     * @return an ActionListener that handles comment posting
     */
    private static ActionListener createCommentActionListener(Post post) {
        return e -> {
            JTextField textField = new JTextField();
            Object[] message = {"Create Comment:", textField};
            int option = JOptionPane.showOptionDialog(
                    null,
                    message,
                    "Create Comment",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new Object[]{"Post", "Cancel"},
                    "Post"
            );
            if (option == JOptionPane.YES_OPTION && !textField.getText().isEmpty()) {
                boolean commentSuccess;
                try {
                    commentSuccess = PlatformRunner.client.createComment(post.getId(), textField.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (commentSuccess)
                    JOptionPane.showMessageDialog(null, "Comment posted: " +
                            textField.getText());
            }
        };
    }
}
