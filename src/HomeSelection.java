import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class HomeSelection {

    public static void mainView(JPanel mainPanel) {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Create the search bar panel
        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.setBackground(Color.WHITE);
        searchBarPanel.setBorder(new EmptyBorder(20, 0, 20, 0)); // Add vertical padding

        // Create the search field
        SearchTextField searchField = new SearchTextField();
        searchField.setPreferredSize(new Dimension(400, 40));

        // Center the search field in the panel
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(searchField);
        searchBarPanel.add(centerPanel, BorderLayout.CENTER);

        // Create the "Create New Post" button
        RoundedButton createPostButton = new RoundedButton("Create New Post");
        createPostButton.setForeground(Color.WHITE);
        createPostButton.setBackground(new Color(0, 122, 255)); // Blue color
        createPostButton.setFont(new Font("Arial", Font.BOLD, 14));
        createPostButton.setFocusPainted(false);
        createPostButton.setBorderPainted(false);
        createPostButton.setOpaque(false);

        // Add action listener for the button
        createPostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showInputDialog("Enter your message:");
            }
        });

        // Add the button to the right (East) of the search bar panel
        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        eastPanel.setBackground(Color.WHITE);
        eastPanel.setBorder(new EmptyBorder(0, 0, 0, 20)); // Add right padding
        eastPanel.add(createPostButton);
        searchBarPanel.add(eastPanel, BorderLayout.EAST);

        // Add the search bar panel to the top of the main panel
        mainPanel.add(searchBarPanel, BorderLayout.NORTH);

        // Add the scrollable content in the CENTER region
        addScrollableContent(mainPanel);
    }

    private static void addScrollableContent(JPanel mainPanel) {
        // Create a panel to hold the content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Arrange posts vertically
        contentPanel.setBackground(Color.WHITE);

        // Add 5 individual posts to the content panel
        for (int i = 1; i <= 5; i++) {
            JPanel post = SinglePost.individualPost("friend"); // Create an individual post
            contentPanel.add(post); // Add the post to the content panel
            contentPanel.add(Box.createVerticalStrut(40)); // Add spacing between posts
        }

        // Wrap the content panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling

        // Remove the border from the scroll pane
        scrollPane.setBorder(null);

        // Add the scroll pane to the CENTER region
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
}