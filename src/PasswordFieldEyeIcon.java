import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;

/**
 * PasswordFieldEyeIcon
 *
 * This class extends JPasswordField and adds a toggleable eye icon button
 * to show or hide the typed password.
 *
 * @author Rachel Rafik, L22
 *
 * @version December 8, 2024
 */
public class PasswordFieldEyeIcon extends JPasswordField {

    private final TransparentJButton eyeButton;    // Button to toggle password visibility
    private final FontIcon eyeOpenIcon;            // Icon for visible password state
    private final FontIcon eyeClosedIcon;          // Icon for hidden password state
    private boolean isPasswordVisible = false;     // Tracks if the password is currently visible

    /**
     * Constructs a PasswordFieldEyeIcon with a default hidden password echo char
     * and an eye icon button to toggle visibility.
     */
    public PasswordFieldEyeIcon() {
        super();
        setEchoChar('•');

        eyeOpenIcon = FontIcon.of(FontAwesome.EYE, 20);
        eyeClosedIcon = FontIcon.of(FontAwesome.EYE_SLASH, 20);

        eyeButton = new TransparentJButton("");
        eyeButton.setIcon(eyeClosedIcon);
        eyeButton.setPreferredSize(new Dimension(30, 30));
        eyeButton.addActionListener(e -> togglePasswordVisibility());

        setLayout(new BorderLayout());
        add(eyeButton, BorderLayout.EAST);
    }

    /**
     * Toggles the password visibility between hidden and visible states.
     */
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            setEchoChar((char) 0);
            eyeButton.setIcon(eyeOpenIcon);
        } else {
            setEchoChar('•');
            eyeButton.setIcon(eyeClosedIcon);
        }
    }
}
