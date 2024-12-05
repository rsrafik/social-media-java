import javax.swing.*;
import java.awt.*;

class HomeSelection {

    public static void mainView(JPanel mainPanel) {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Create the search bar panel
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for centering
        searchBarPanel.setBackground(Color.WHITE);

        // Set fixed height for the search bar panel
        searchBarPanel.setPreferredSize(new Dimension(0, 100));

        // Calculate the initial width dynamically

        // Create the search field with the calculated initial width
        SearchTextField searchField = new SearchTextField();
        searchField.setPreferredSize(new Dimension(400, 40)); // Set the size dynamically

        // Add search field to the center of the search bar panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Center horizontally
        gbc.gridy = 0; // Center vertically
        gbc.anchor = GridBagConstraints.CENTER; // Align to center
        searchBarPanel.add(searchField, gbc);

        // Add search bar panel to the top of the main panel
        mainPanel.add(searchBarPanel, BorderLayout.NORTH);


        // Add the scrollable content in the SOUTH region
        addScrollableContent(mainPanel);
    }

    private static void addScrollableContent(JPanel mainPanel) {
        // Create a panel to hold the content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Arrange posts vertically
        contentPanel.setBackground(Color.WHITE);

        // Add 5 individual posts to the content panel
        for (int i = 1; i <= 5; i++) {
            JPanel post = SinglePost.individualPost(); // Create an individual post
            contentPanel.add(Box.createVerticalStrut(40)); // Add spacing between posts
            contentPanel.add(post); // Add the post to the content panel
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
