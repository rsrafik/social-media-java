import javax.swing.*;
import java.awt.*;

class InboxSelection {
    public static void mainView(JPanel mainPanel) {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Your Inbox Messages", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        mainPanel.add(label, BorderLayout.CENTER);
    }
}

