import javax.swing.*;
import java.awt.*;

/**
 * RoundedButton
 *
 * This class creates a JButton with rounded corners and custom insets for padding.
 * The button supports antialiasing for smooth edges and can be styled by setting
 * background and foreground colors.
 *
 * @author Rachel Rafik, L22
 * @version December 8, 2024
 */
class RoundedButton extends JButton {

    /**
     * Constructs a RoundedButton with the specified text.
     *
     * @param text The text to display on the button.
     */
    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
    }

    /**
     * Paints the component with rounded edges and antialiasing.
     *
     * @param g the Graphics context in which to paint
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        super.paintComponent(g);
        g2.dispose();
    }

    /**
     * Paints the border of the button with rounded edges.
     *
     * @param g the Graphics context in which to paint
     */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        g2.dispose();
    }

    /**
     * Returns the insets for the button to provide padding.
     *
     * @return the insets for the button
     */
    @Override
    public Insets getInsets() {
        return new Insets(10, 20, 10, 20);
    }
}
