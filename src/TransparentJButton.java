import javax.swing.*;

public class TransparentJButton extends JButton {

    public TransparentJButton (String text){
        super(text);
        setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setFocusPainted(false);
    }
}