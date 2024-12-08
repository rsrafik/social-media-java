import javax.swing.*;
import java.awt.*;

/**
 * ScrollableOptionPane
 * <p>
 * This class creates a JOptionPane that displays a scrollable list of messages.
 * It is useful when the number of messages is large and would otherwise not fit
 * in a single dialog without scrolling.
 *
 * @author Rachel Rafik, L22
 * @version December 8, 2024
 */
public class ScrollableOptionPane extends JOptionPane {

    /**
     * Constructs a ScrollableOptionPane with the given array of messages.
     *
     * @param messages the array of messages to display
     */
    public ScrollableOptionPane(String[] messages) {
        JPanel commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));

        for (String message : messages) {
            JLabel commentLabel = new JLabel(message);
            commentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            commentsPanel.add(commentLabel);
        }

        JScrollPane scrollPane = new JScrollPane(commentsPanel);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        setMessage(scrollPane);
        setMessageType(PLAIN_MESSAGE);
    }

    /**
     * Displays a dialog with a scrollable list of messages.
     *
     * @param parentComponent the parent component of the dialog
     * @param messages        the array of messages to display
     * @param title           the title of the dialog
     */
    public static void showDialog(Component parentComponent, String[] messages, String title) {
        ScrollableOptionPane optionPane = new ScrollableOptionPane(messages);
        JDialog dialog = optionPane.createDialog(parentComponent, title);
        dialog.setVisible(true);
    }
}
