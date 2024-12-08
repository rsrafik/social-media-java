import javax.swing.*;
import java.awt.*;

public class ScrollableOptionPane extends JOptionPane {

    public ScrollableOptionPane(String[] messages) {
        // Create a JPanel to hold the comments
        JPanel commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS)); // Vertical layout

        // Add each comment as a JLabel to the panel
        for (String message : messages) {
            JLabel commentLabel = new JLabel(message);
            commentLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align labels to the left
            commentsPanel.add(commentLabel);
        }

        // Wrap the JPanel in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(commentsPanel);
        scrollPane.setPreferredSize(new Dimension(400, 200)); // Set preferred size for scrollable area

        // Set up the JOptionPane with the JScrollPane as the message
        setMessage(scrollPane);
        setMessageType(PLAIN_MESSAGE); // Use a plain message to suppress icons
    }

    // Utility method to show the dialog
    public static void showDialog(Component parentComponent, String[] messages, String title) {
        ScrollableOptionPane optionPane = new ScrollableOptionPane(messages);
        JDialog dialog = optionPane.createDialog(parentComponent, title);
        dialog.setVisible(true);
    }
}
