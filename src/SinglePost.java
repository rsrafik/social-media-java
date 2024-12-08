import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class SinglePost {
    private final String username;
    private final String message;

    public SinglePost(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public static JPanel individualPost(String role) {
        // Create a panel to represent the post
        JPanel postPanel = new JPanel();
        postPanel.setPreferredSize(new Dimension(800, 200)); // Fixed size: width = 800, height = 200
        postPanel.setMaximumSize(new Dimension(800, 200)); // Ensure the size does not expand
        postPanel.setBackground(new Color(230, 230, 230)); // Light gray background
        postPanel.setLayout(new BorderLayout()); // Use BorderLayout to center the text

        // User topper (top bar)
        JPanel userTopper = new JPanel(new BorderLayout());
        userTopper.setPreferredSize(new Dimension(postPanel.getWidth(), 45));
        userTopper.setBackground(new Color(154, 154, 154));

        // Username label on the left
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBorder(new EmptyBorder(0, 20, 0, 0)); // Add left padding
        userTopper.add(usernameLabel, BorderLayout.WEST);

        // Three dots button on the right
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new BorderLayout()); // Simplify layout for alignment
        buttonHolder.setBackground(new Color(154, 154, 154)); // Match userTopper background
        buttonHolder.setBorder(new EmptyBorder(0, 0, 0, 5)); // Reduce padding to align closer to the edge

        JButton moreButton = new JButton("•••");
        moreButton.setFont(new Font("Arial", Font.BOLD, 20));
        moreButton.setForeground(Color.WHITE);
        moreButton.setFocusPainted(false);
        moreButton.setContentAreaFilled(false);
        moreButton.setBorderPainted(false);
        moreButton.setHorizontalAlignment(SwingConstants.RIGHT);

        moreButton.addActionListener(e -> {
            String message = role.equalsIgnoreCase("friend") ? "Hide post?" : "Delete post?";
            int result = JOptionPane.showConfirmDialog(
                    postPanel,
                    message,
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                System.out.println(message.equals("Hide post?") ? "Post hidden!" : "Post deleted!");
            }
        });

        buttonHolder.add(moreButton, BorderLayout.CENTER); // Center alignment
        userTopper.add(buttonHolder, BorderLayout.EAST);

        // Add the userTopper to the top of the post panel
        postPanel.add(userTopper, BorderLayout.NORTH);

        // User bottomer (bottom bar)
        JPanel userBottomer = new JPanel(new BorderLayout());
        userBottomer.setPreferredSize(new Dimension(postPanel.getWidth(), 30));
        userBottomer.setBackground(new Color(197, 197, 197));

        // Left section: Upvote/Downvote
        JPanel votePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        votePanel.setBackground(new Color(197, 197, 197));

        JLabel upvoteLabel = new JLabel("1");
        upvoteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JToggleButton upvoteButton = new JToggleButton("▲");
        upvoteButton.setFocusPainted(false);
        upvoteButton.setForeground(new Color(66, 66, 66, 154));

        JLabel downvoteLabel = new JLabel("0");
        downvoteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JToggleButton downvoteButton = new JToggleButton("▼");
        downvoteButton.setFocusPainted(false);
        downvoteButton.setForeground(new Color(66, 66, 66, 154));

        AtomicBoolean isUpvoted = new AtomicBoolean(false);
        AtomicBoolean isDownvoted = new AtomicBoolean(false);

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

        votePanel.add(upvoteLabel);
        votePanel.add(upvoteButton);
        votePanel.add(Box.createHorizontalStrut(10));
        votePanel.add(downvoteLabel);
        votePanel.add(downvoteButton);

        // Right section: Comment buttons
        JPanel commentPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        commentPanel.setBackground(new Color(197, 197, 197));
        JButton makeComment = new JButton("Add Comment");
        makeComment.setForeground(new Color(53, 53, 53,154));
        makeComment.setFocusPainted(false);
        makeComment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField textField = new JTextField();
                Object[] message = {"Create Comment:", textField};

                // Show a dialog with custom "Post" and "Cancel" buttons
                int option = JOptionPane.showOptionDialog(
                        null,
                        message,
                        "Create Comment",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        new Object[]{"Post", "Cancel"}, // Custom buttons
                        "Post" // Default button
                );


                if (option == JOptionPane.YES_OPTION) { // "Post" button clicked
                    String input = textField.getText();
                    if (!input.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Message posted: " + input);
                    } else {
                        JOptionPane.showMessageDialog(null, "No message entered!");
                    }
                }
            }
        });

        String[] comments = {
                "user1: This is a comment.",
                "user2: Another comment here.",
                "user3: Yet another insightful comment.",
                "user4: Keep the comments coming!",
                "user5: Here's my contribution.",
                "user6: I love these discussions.",
                "user7: Let's add more comments.",
                "user8: This is so cool!",
                "user9: Amazing work everyone.",
                "user10: I agree with all of this.",
                "user1: This is a comment.",
                "user2: Another comment here.",
                "user3: Yet another insightful comment.",
                "user4: Keep the comments coming!",
                "user5: Here's my contribution.",
                "user6: I love these discussions.",
                "user7: Let's add more comments.",
                "user8: This is so cool!",
                "user9: Amazing work everyone.",
                "user10: I agree with all of this.",
        };

        JButton viewComments = new JButton("View Comments");
        viewComments.setForeground(new Color(53, 53, 53,154));
        viewComments.setFocusPainted(false);
        viewComments.addActionListener(e -> {
            ScrollableOptionPane.showDialog(null, comments, "Comments");
        });

        commentPanel.add(makeComment);
        commentPanel.add(viewComments);

        userBottomer.add(votePanel, BorderLayout.WEST);
        userBottomer.add(commentPanel, BorderLayout.EAST);

        postPanel.add(userBottomer, BorderLayout.SOUTH);

        return postPanel;
    }
}
