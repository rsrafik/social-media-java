import javax.swing.*;
import java.awt.*;

public class SinglePost {
    public static JPanel individualPost() {
        // Create a panel to represent the post
        JPanel postPanel = new JPanel();
        postPanel.setPreferredSize(new Dimension(800, 400)); // Fixed size: width = 800, height = 400
        postPanel.setMaximumSize(new Dimension(800, 400)); // Ensure the size does not expand
        postPanel.setBackground(new Color(230, 230, 230)); // Light gray background
        postPanel.setLayout(new BorderLayout()); // Use BorderLayout to center the text

        // Create a label for the text
        JLabel label = new JLabel("Hi", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20)); // Set font style and size
        postPanel.add(label, BorderLayout.CENTER); // Add the label to the center of the panel

        return postPanel; // Return the post panel
    }
}
