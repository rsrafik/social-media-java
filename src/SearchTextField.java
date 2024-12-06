import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SearchTextField extends JTextField {

    private final TransparentJButton searchButton;
    private final FontIcon searchIcon;

    public SearchTextField() {
        super(); // Call the constructor of JTextField

        // Set up placeholder text
        setText("Search here...");

        setForeground(Color.GRAY);

        // Add focus listener to manage placeholder behavior
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals("Search here...")) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText("Search here...");
                    setForeground(Color.GRAY);
                }
            }
        });

        // Create FontAwesome search icon
        searchIcon = FontIcon.of(FontAwesome.SEARCH, 16);

        // Create the search button
        searchButton = new TransparentJButton(""); // No text, only icon
        searchButton.setIcon(searchIcon); // Set the search icon
        searchButton.setPreferredSize(new Dimension(30, 30)); // Set button size

        // Style the search field
        setBackground(new Color(230, 230, 230)); // Light gray background
        setBorder(new EmptyBorder(5, 10, 5, 40)); // Add padding inside
        setPreferredSize(new Dimension(200, 30)); // Set field size

        // Set up rounded edges
        setOpaque(false);

        // Add the button to the field's parent container
        setLayout(new BorderLayout());
        add(searchButton, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Paint rounded rectangle for the text field background
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded corners
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border to paint
    }

    // Remove the @Override annotation for setBorder
    public void setBorder(Border border) {
        // Ignore default border setting
    }

    @Override
    public Insets getInsets() {
        // Provide custom insets for padding inside the text field
        return new Insets(10, 10, 10, 5); // Top, Left (5px padding), Bottom, Right
    }
}
