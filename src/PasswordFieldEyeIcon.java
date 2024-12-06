import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;

public class PasswordFieldEyeIcon extends JPasswordField {
    private final TransparentJButton eyeButton;
    private final FontIcon eyeOpenIcon;
    private final FontIcon eyeClosedIcon;
    private boolean isPasswordVisible = false;

    public PasswordFieldEyeIcon() {
        super(); // Call the constructor of JPasswordField
        setEchoChar('•'); // Default hidden password character

        // Create FontAwesome icons
        eyeOpenIcon = FontIcon.of(FontAwesome.EYE, 20); // Open eye icon
        eyeClosedIcon = FontIcon.of(FontAwesome.EYE_SLASH, 20); // Closed eye icon

        // Create the eye button
        eyeButton = new TransparentJButton(""); // No text, only icon
        eyeButton.setIcon(eyeClosedIcon); // Set initial icon
        eyeButton.setPreferredSize(new Dimension(30, 30)); // Set button size
        eyeButton.addActionListener(e -> togglePasswordVisibility());

        // Add the button to the field's parent container
        setLayout(new BorderLayout());
        add(eyeButton, BorderLayout.EAST);
    }

    // Toggles password visibility
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            setEchoChar((char) 0); // Show password
            eyeButton.setIcon(eyeOpenIcon); // Switch to open eye icon
        } else {
            setEchoChar('•'); // Hide password
            eyeButton.setIcon(eyeClosedIcon); // Switch to closed eye icon
        }
    }
}
