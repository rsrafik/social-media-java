import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

class HomeSelection {

    private static JPanel mainContentPanel; // Panel to dynamically switch content
    private static JScrollPane currentScrollPane; // Keeps track of the current scrollable content

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

        // Set search action listener to switch to OtherProfileSelection
        searchField.setSearchActionListener(selectedOption -> {
            try {
                switchToOtherProfileView();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

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

        createPostButton.addActionListener(e -> {
            JTextField textField = new JTextField();
            Object[] message = {"Create Post:", textField};

            // Show a dialog with custom "Post" and "Cancel" buttons
            int option = JOptionPane.showOptionDialog(
                    null,
                    message,
                    "Create Post",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new Object[]{"Post", "Cancel"}, // Custom buttons
                    "Post" // Default button
            );

            if (option == JOptionPane.YES_OPTION) { // "Post" button clicked
                String input = textField.getText();
                if (!input.isEmpty()) {
                    boolean postSuccess;

                    try {
                        postSuccess = PlatformRunner.client.createPost(input,null);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    if (postSuccess)
                        JOptionPane.showMessageDialog(null, "Message posted: " + input);
                } else {
                    JOptionPane.showMessageDialog(null, "No message entered!");
                }
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

        // Main content panel (center area)
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(Color.WHITE);
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        // Add the initial scrollable content
        currentScrollPane = createScrollableContent();
        mainContentPanel.add(currentScrollPane, BorderLayout.CENTER);
    }

    private static JScrollPane createScrollableContent() {
        // Create a panel to hold the content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Arrange posts vertically
        contentPanel.setBackground(Color.WHITE);

        // Add 5 individual posts to the content panel
//        for (int i = 1; i <= 5; i++) {
//            JPanel post = SinglePost.individualPost("Username!!", "Message!!","friend"); // Create an individual post
//            contentPanel.add(post); // Add the post to the content panel
//            contentPanel.add(Box.createVerticalStrut(40)); // Add spacing between posts
//        }

        // Wrap the content panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        scrollPane.setBorder(null); // Remove the border from the scroll pane

        return scrollPane;
    }

    private static void switchToOtherProfileView() throws IOException, ClassNotFoundException {
        // Remove any existing content, including OtherProfileSelection if it was already displayed
        mainContentPanel.removeAll();

        // Create a new panel for the OtherProfileSelection
        JPanel otherProfilePanel = new JPanel(new BorderLayout());
        OtherProfileSelection.mainView(otherProfilePanel); // Populate the panel using OtherProfileSelection

        // Add the otherProfilePanel to the main content panel
        mainContentPanel.add(otherProfilePanel, BorderLayout.CENTER);

        // Refresh the layout
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

}
