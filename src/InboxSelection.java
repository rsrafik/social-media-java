import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class InboxSelection {

    public static void mainView(JPanel mainPanel) {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Add the title at the top
        JLabel titleLabel = new JLabel("Inbox");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create and add the scrollable inbox content
        JScrollPane inboxScrollPane = createInboxScrollPane();
        mainPanel.add(inboxScrollPane, BorderLayout.CENTER);
    }

    private static JScrollPane createInboxScrollPane() {
        // Create a panel to hold the inbox rows
        JPanel inboxPanel = new JPanel();
        inboxPanel.setLayout(new BoxLayout(inboxPanel, BoxLayout.Y_AXIS));
        inboxPanel.setBackground(Color.WHITE);
        inboxPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add margins around the list

        // Add sample usernames
        for (int i = 1; i <= 20; i++) {
            inboxPanel.add(createInboxRow("user" + i));
            inboxPanel.add(Box.createVerticalStrut(10)); // Consistent spacing between rows
        }

        // Wrap the panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(inboxPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        scrollPane.setBorder(null);

        return scrollPane;
    }

    //MAKE AN INBOX
    private static JPanel createInboxRow(String username) {
        JPanel rowPanel = new JPanel(new GridBagLayout());
        rowPanel.setBorder(new EmptyBorder(5, 10, 5, 10)); // Add padding
        rowPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE; // Retain components' preferred sizes
        gbc.insets = new Insets(0, 5, 0, 5); // Minimal spacing between components

        // Username label
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; // First column
        gbc.gridy = 0;
        rowPanel.add(usernameLabel, gbc);

        // Accept button
        JButton acceptButton = new JButton("Accept");
        acceptButton.setFont(new Font("Arial", Font.PLAIN, 14));
        acceptButton.setFocusPainted(false);
        acceptButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(rowPanel, username + " accepted!");
        });
        gbc.gridx = 1; // Second column
        gbc.gridy = 0;
        rowPanel.add(acceptButton, gbc);

        // Reject button
        JButton rejectButton = new JButton("Reject");
        rejectButton.setFont(new Font("Arial", Font.PLAIN, 14));
        rejectButton.setFocusPainted(false);
        rejectButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(rowPanel, username + " rejected!");
        });
        gbc.gridx = 2; // Third column
        gbc.gridy = 0;
        rowPanel.add(rejectButton, gbc);

        return rowPanel;
    }
}
