import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;

public class SinglePost {
    public static JPanel individualPost(Post post, String role) throws IOException, ClassNotFoundException {
        // Create a panel to represent the post
        JPanel postPanel = new JPanel();
        postPanel.setBackground(new Color(230, 230, 230)); // Light gray background
        postPanel.setLayout(new BorderLayout()); // Use BorderLayout to organize components

        // User topper (top bar)
        JPanel userTopper = new JPanel(new BorderLayout());
        userTopper.setPreferredSize(new Dimension(800, 45));
        userTopper.setBackground(new Color(154, 154, 154));

        UserInfo userInfo = PlatformRunner.client.fetchUserInfo(post.getCreatorId());

        // Username label on the left
        JLabel usernameLabel = new JLabel(userInfo.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBorder(new EmptyBorder(0, 20, 0, 0)); // Add left padding
        userTopper.add(usernameLabel, BorderLayout.WEST);

        // Three dots button on the right
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
                System.out.println(moreOption.equals("Hide post?") ? "Post hidden!" : "Post deleted!");
            }
        });
        userTopper.add(moreButton, BorderLayout.EAST);

        // Add the userTopper to the top of the post panel
        postPanel.add(userTopper, BorderLayout.NORTH);

        // Middle content section (dynamic text with padding)
        JLabel placeholderLabel = new JLabel(
                "<html><div style='text-align: center; padding-top: 15px; padding-bottom: 15px;'>" + post.getContent() + "</div></html>",
                SwingConstants.CENTER
        );
        placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        placeholderLabel.setForeground(new Color(70, 70, 70));

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(new Color(230, 230, 230)); // Match postPanel background
        middlePanel.add(placeholderLabel, BorderLayout.CENTER);

        // Calculate dynamic height based on placeholder text
        placeholderLabel.setSize(800, Short.MAX_VALUE); // Set a fixed width, unlimited height for size calculation
        Dimension labelSize = placeholderLabel.getPreferredSize();
        int dynamicHeight = 45 + labelSize.height + 30; // Topper height + label height + bottomer height
        postPanel.setPreferredSize(new Dimension(800, dynamicHeight));
        postPanel.setMaximumSize(new Dimension(800, dynamicHeight));

        postPanel.add(middlePanel, BorderLayout.CENTER);

        // User bottomer (bottom bar)
        JPanel userBottomer = new JPanel(new BorderLayout());
        userBottomer.setPreferredSize(new Dimension(800, 30));
        userBottomer.setBackground(new Color(197, 197, 197));

        // Left section: Upvote/Downvote
        JPanel votePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        votePanel.setBackground(new Color(197, 197, 197));

        JLabel upvoteLabel = new JLabel("1");
        upvoteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JToggleButton upvoteButton = new JToggleButton("▲");
        upvoteButton.setFocusPainted(false);

        JLabel downvoteLabel = new JLabel("0");
        downvoteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JToggleButton downvoteButton = new JToggleButton("▼");
        downvoteButton.setFocusPainted(false);

        AtomicBoolean isUpvoted = new AtomicBoolean(false);
        AtomicBoolean isDownvoted = new AtomicBoolean(false);

        addVoteButtonListeners(upvoteButton, downvoteButton, upvoteLabel, downvoteLabel, isUpvoted, isDownvoted);

        votePanel.add(upvoteLabel);
        votePanel.add(upvoteButton);
        votePanel.add(Box.createHorizontalStrut(10));
        votePanel.add(downvoteLabel);
        votePanel.add(downvoteButton);

        // Right section: Comment buttons
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
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            List<String> commentMessages = new ArrayList<>();

            if (commentList != null) {
                for (Comment comment : commentList) {
                    try {
                        commentMessages.add(PlatformRunner.client.fetchUserInfo(comment.getCreatorId()).getUsername() + ": " + comment.getContent());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
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

    private static void addVoteButtonListeners(
            JToggleButton upvoteButton,
            JToggleButton downvoteButton,
            JLabel upvoteLabel,
            JLabel downvoteLabel,
            AtomicBoolean isUpvoted,
            AtomicBoolean isDownvoted
    ) {
        upvoteButton.addActionListener(e -> {
            if (!isUpvoted.get()) {
                upvoteLabel.setText(String.valueOf(Integer.parseInt(upvoteLabel.getText()) + 1));
                if (isDownvoted.get()) {
                    downvoteLabel.setText(String.valueOf(Integer.parseInt(downvoteLabel.getText()) - 1));
                }
                isUpvoted.set(true);
                isDownvoted.set(false);
            } else {
                upvoteLabel.setText(String.valueOf(Integer.parseInt(upvoteLabel.getText()) - 1));
                isUpvoted.set(false);
            }
            downvoteButton.setSelected(false);
        });

        downvoteButton.addActionListener(e -> {
            if (!isDownvoted.get()) {
                downvoteLabel.setText(String.valueOf(Integer.parseInt(downvoteLabel.getText()) + 1));
                if (isUpvoted.get()) {
                    upvoteLabel.setText(String.valueOf(Integer.parseInt(upvoteLabel.getText()) - 1));
                }
                isDownvoted.set(true);
                isUpvoted.set(false);
            } else {
                downvoteLabel.setText(String.valueOf(Integer.parseInt(downvoteLabel.getText()) - 1));
                isDownvoted.set(false);
            }
            upvoteButton.setSelected(false);
        });
    }

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
                    commentSuccess = PlatformRunner.client.createComment(post.getId(),textField.getText());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (commentSuccess)
                    JOptionPane.showMessageDialog(null, "Comment posted: " + textField.getText());
            }
        };
    }
}
