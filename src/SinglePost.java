import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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
        postPanel.setPreferredSize(new Dimension(800, 200)); // Fixed size: width = 800, height = 400
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
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.Y_AXIS));
        buttonHolder.setBackground(new Color(154, 154, 154, 154));
        buttonHolder.setBorder(new EmptyBorder(0, 0, 0, 20)); // Add right padding

        TransparentJButton moreButton = new TransparentJButton("•••");
        moreButton.setFont(new Font("Arial", Font.BOLD, 20));
        moreButton.setForeground(Color.WHITE);
        moreButton.setBackground(new Color(154, 154, 154)); // Matches userTopper background
        moreButton.setOpaque(false); // Ensure transparency
        moreButton.setFocusPainted(false); // Disable focus highlight
        moreButton.setContentAreaFilled(false); // Disable content filling
        moreButton.setBorderPainted(false); // Disable border painting
        moreButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally in BoxLayout

        // Add action listener to show different JOptionPane messages
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

        buttonHolder.add(Box.createVerticalGlue()); // Add glue to push content to the center
        buttonHolder.add(moreButton);
        buttonHolder.add(Box.createVerticalGlue()); // Add glue to balance spacing

        userTopper.add(buttonHolder, BorderLayout.EAST);

        // Add the userTopper to the top of the post panel
        postPanel.add(userTopper, BorderLayout.NORTH);

        // User bottomer (bottom bar)
        JPanel userBottomer = new JPanel();
        userBottomer.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0)); // FlowLayout with left alignment and spacing
        userBottomer.setPreferredSize(new Dimension(postPanel.getWidth(), 30));
        userBottomer.setBackground(new Color(197, 197, 197));

        // Upvote section
        JLabel upvoteLabel = new JLabel("1");
        upvoteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JToggleButton upvoteButton = new JToggleButton("▲");
        upvoteButton.setFocusPainted(false);
        upvoteButton.setForeground(new Color(66, 66, 66, 154));

        // Downvote section
        JLabel downvoteLabel = new JLabel("0");
        downvoteLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JToggleButton downvoteButton = new JToggleButton("▼");
        downvoteButton.setFocusPainted(false);
        downvoteButton.setForeground(new Color(66, 66, 66, 154));

        // Add mutual exclusivity to the buttons
        upvoteButton.addActionListener(e -> {
            if (upvoteButton.isSelected()) {
                downvoteButton.setSelected(false);
                upvoteLabel.setText(String.valueOf(Integer.parseInt(upvoteLabel.getText()) + 1));
            } else {
                upvoteLabel.setText(String.valueOf(Integer.parseInt(upvoteLabel.getText()) - 1));
            }
        });

        downvoteButton.addActionListener(e -> {
            if (downvoteButton.isSelected()) {
                upvoteButton.setSelected(false);
                downvoteLabel.setText(String.valueOf(Integer.parseInt(downvoteLabel.getText()) + 1));
            } else {
                downvoteLabel.setText(String.valueOf(Integer.parseInt(downvoteLabel.getText()) - 1));
            }
        });

        userBottomer.add(upvoteLabel);
        userBottomer.add(upvoteButton);
        userBottomer.add(Box.createHorizontalStrut(10));
        userBottomer.add(downvoteLabel);
        userBottomer.add(downvoteButton);

        // Add the userBottomer to the bottom of the post panel
        postPanel.add(userBottomer, BorderLayout.SOUTH);

        return postPanel; // Return the post panel
    }
}
