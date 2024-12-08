import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * HomeSelection
 *
 * This class manages the main home view of the application, including
 * a search bar, the ability to create posts, and a scrollable feed.
 *
 * @author Rachel Rafik, L22
 *
 * @version December 8, 2024
 */
class HomeSelection {

    private static JPanel mainContentPanel;      // Panel to dynamically switch content
    private static JScrollPane currentScrollPane; // Keeps track of the current scrollable content

    /**
     * Sets up and displays the main home view.
     *
     * @param mainPanel the panel to which UI elements will be added
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if a class cannot be found
     */
    public static void mainView(JPanel mainPanel) throws IOException, ClassNotFoundException {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.setBackground(Color.WHITE);
        searchBarPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        SearchTextField searchField = new SearchTextField();
        searchField.setPreferredSize(new Dimension(400, 40));
        searchField.setSearchActionListener(selectedOption -> {
            try {
                switchToOtherProfileView();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(searchField);
        searchBarPanel.add(centerPanel, BorderLayout.CENTER);

        RoundedButton createPostButton = new RoundedButton("Create New Post");
        createPostButton.setForeground(Color.WHITE);
        createPostButton.setBackground(new Color(0, 122, 255));
        createPostButton.setFont(new Font("Arial", Font.BOLD, 14));
        createPostButton.setFocusPainted(false);
        createPostButton.setBorderPainted(false);
        createPostButton.setOpaque(false);

        createPostButton.addActionListener(e -> {
            JTextField textField = new JTextField();
            Object[] message = {"Create Post:", textField};

            int option = JOptionPane.showOptionDialog(
                    null,
                    message,
                    "Create Post",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    new Object[]{"Post", "Cancel"},
                    "Post"
            );

            if (option == JOptionPane.YES_OPTION) {
                String input = textField.getText();
                if (!input.isEmpty()) {
                    boolean postSuccess;
                    try {
                        postSuccess = PlatformRunner.client.createPost(input, null);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    if (postSuccess) {
                        JOptionPane.showMessageDialog(null, "Message posted: " + input);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No message entered!");
                }
            }
        });

        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        eastPanel.setBackground(Color.WHITE);
        eastPanel.setBorder(new EmptyBorder(0, 0, 0, 20));
        eastPanel.add(createPostButton);
        searchBarPanel.add(eastPanel, BorderLayout.EAST);

        mainPanel.add(searchBarPanel, BorderLayout.NORTH);

        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(Color.WHITE);
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        currentScrollPane = createScrollableContent();
        mainContentPanel.add(currentScrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates and returns a scroll pane containing a feed of posts.
     *
     * @return the scroll pane containing posts
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if a class cannot be found
     */
    private static JScrollPane createScrollableContent() throws IOException, ClassNotFoundException {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        List<Post> loadPost = PlatformRunner.client.loadFeed();
        for (int i = 0; i < loadPost.size(); i++) {
            JPanel post = SinglePost.individualPost(loadPost.get(i), "friend");
            contentPanel.add(post);
            contentPanel.add(Box.createVerticalStrut(40));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        return scrollPane;
    }

    /**
     * Switches to the other profile view.
     *
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if a class cannot be found
     */
    private static void switchToOtherProfileView() throws IOException, ClassNotFoundException {
        mainContentPanel.removeAll();
        mainContentPanel.revalidate();
        mainContentPanel.repaint();

        JPanel otherProfilePanel = new JPanel(new BorderLayout());
        OtherProfileSelection.mainView(otherProfilePanel);

        mainContentPanel.add(otherProfilePanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

}
