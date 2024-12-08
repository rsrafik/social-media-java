import javax.swing.*;

/**
 * TransparentJButton
 * <p>
 * This class creates a JButton with no border, no background,
 * and a transparent look. It is useful for creating overlay
 * buttons or buttons that blend into their backgrounds.
 *
 * @author Rachel Rafik, L22
 * @version December 8, 2024
 */
public class TransparentJButton extends JButton {

    /**
     * Constructs a TransparentJButton with the specified text.
     *
     * @param text the text to be displayed on the button
     */
    public TransparentJButton(String text) {
        super(text);
        setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setFocusPainted(false);
    }
}
