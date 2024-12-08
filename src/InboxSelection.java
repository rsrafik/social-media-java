import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class InboxSelection {
    private static JPanel mainPanelRef;

    public static void mainView(JPanel mainPanel) throws IOException, ClassNotFoundException {
        mainPanelRef = mainPanel; // Store reference
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Inbox");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JScrollPane inboxScrollPane = createInboxScrollPane();
        mainPanel.add(inboxScrollPane, BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private static JScrollPane createInboxScrollPane() throws IOException, ClassNotFoundException {
        JPanel inboxPanel = new JPanel();
        inboxPanel.setLayout(new BoxLayout(inboxPanel, BoxLayout.Y_AXIS));
        inboxPanel.setBackground(Color.WHITE);
        inboxPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        List<Integer> inboxIds = PlatformRunner.client.getFollowRequests();
        List<UserInfo> inboxUsers = new ArrayList<>();

        for(int inboxId : inboxIds) {
            inboxUsers.add(PlatformRunner.client.fetchUserInfo(inboxId));
        }

        for (UserInfo user : inboxUsers) {
            inboxPanel.add(createInboxRow(user));
            inboxPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(inboxPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        return scrollPane;
    }

    private static JPanel createInboxRow(UserInfo userInfo) {
        JPanel rowPanel = new JPanel(new GridBagLayout());
        rowPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        rowPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 5, 0, 5);

        JLabel usernameLabel = new JLabel(userInfo.getUsername());
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        rowPanel.add(usernameLabel, gbc);

        JButton acceptButton = new JButton("Accept");
        acceptButton.setFont(new Font("Arial", Font.PLAIN, 14));
        acceptButton.setFocusPainted(false);
        acceptButton.addActionListener(e -> {
            try {
                boolean acceptedSuccess = PlatformRunner.client.acceptFollowRequest(userInfo.getId());
                if (acceptedSuccess) {
                    JOptionPane.showMessageDialog(rowPanel, userInfo.getUsername() + " accepted!");
                    refreshInbox();
                } else {
                    System.out.println("false");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        gbc.gridx = 1;
        rowPanel.add(acceptButton, gbc);

        JButton rejectButton = new JButton("Reject");
        rejectButton.setFont(new Font("Arial", Font.PLAIN, 14));
        rejectButton.setFocusPainted(false);
        rejectButton.addActionListener(e -> {
            try {
                boolean rejectedSuccess = PlatformRunner.client.rejectFollowRequest(userInfo.getId());
                if (rejectedSuccess) {
                    JOptionPane.showMessageDialog(rowPanel, userInfo.getUsername() + " rejected!");
                    refreshInbox();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        gbc.gridx = 2;
        rowPanel.add(rejectButton, gbc);

        return rowPanel;
    }

    // Call this after accept/reject to refresh the inbox.
    private static void refreshInbox() {
        mainPanelRef.removeAll();
        try {
            mainView(mainPanelRef);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        mainPanelRef.revalidate();
        mainPanelRef.repaint();
    }
}
