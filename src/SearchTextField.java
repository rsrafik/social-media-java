import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchTextField
 *
 * This class extends JTextField and allows searching for users.
 * It displays a placeholder when empty, toggles the search button state
 * based on text input, and shows a dialog with search results.
 * The user can select a user from the dropdown, triggering a listener.
 *
 * @author Rachel Rafik, L22
 * @version December 8, 2024
 */
public class SearchTextField extends JTextField {
    public static UserInfo chosenOne;
    private final TransparentJButton searchButton;
    private final FontIcon searchIcon;

    /**
     * Interface for handling search actions.
     */
    public interface SearchActionListener {
        /**
         * Called when a search is performed and a user is selected.
         *
         * @param selectedOption the selected user's username
         */
        void onSearch(String selectedOption);
    }

    private SearchActionListener searchActionListener;

    /**
     * Constructs a SearchTextField with a placeholder and a search button.
     */
    public SearchTextField() {
        super();
        setText("Search here...");
        setForeground(Color.GRAY);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals("Search here...")) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText("Search here...");
                    setForeground(Color.GRAY);
                }
            }
        });

        searchIcon = FontIcon.of(FontAwesome.SEARCH, 16);

        searchButton = new TransparentJButton("");
        searchButton.setIcon(searchIcon);
        searchButton.setPreferredSize(new Dimension(30, 30));
        searchButton.setEnabled(false);

        searchButton.addActionListener(e -> {
            try {
                handleSearchAction();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                toggleSearchButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                toggleSearchButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                toggleSearchButton();
            }

            private void toggleSearchButton() {
                searchButton.setEnabled(!getText().trim().isEmpty());
            }
        });

        setBackground(new Color(230, 230, 230));
        setBorder(new EmptyBorder(5, 10, 5, 40));
        setPreferredSize(new Dimension(200, 30));
        setOpaque(false);
        setLayout(new BorderLayout());
        add(searchButton, BorderLayout.EAST);
    }

    /**
     * Sets the search action listener.
     *
     * @param listener the listener to handle search actions
     */
    public void setSearchActionListener(SearchActionListener listener) {
        this.searchActionListener = listener;
    }

    /**
     * Handles the search action by fetching users, displaying a dialog, and selecting a user.
     *
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if a required class is not found
     */
    private void handleSearchAction() throws IOException, ClassNotFoundException {
        String searchText = getText();
        List<UserInfo> results = PlatformRunner.client.searchUser(searchText);
        List<String> usernames = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getId().equals(PlatformRunner.client.fetchLoggedInUser().getId()))
                results.remove(i);
        }

        for (UserInfo result : results) {
            usernames.add(result.getUsername());
        }

        JComboBox<String> dropdown = new JComboBox<>(usernames.toArray(new String[0]));

        Object[] message = {
                "\nSearch for: " + searchText,
                "Select a user:",
                dropdown,
                "\n"
        };

        int result = JOptionPane.showOptionDialog(
                null,
                message,
                "Search",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Search", "Cancel"},
                "Search"
        );

        if (result == JOptionPane.YES_OPTION && searchActionListener != null) {
            String selectedOption = (String) dropdown.getSelectedItem();
            UserInfo selectedUser = null;

            for (int i = 0; i < usernames.size(); i++) {
                if (selectedOption != null && selectedOption.equals(usernames.get(i))) {
                    selectedUser = results.get(i);
                    break;
                }
            }

            chosenOne = selectedUser;
            searchActionListener.onSearch(selectedOption);
        }
    }

    /**
     * Returns the search button.
     *
     * @return the search button
     */
    public TransparentJButton getSearchButton() {
        return searchButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border
    }

    @Override
    public void setBorder(Border border) {
        // Ignore default border setting
    }

    @Override
    public Insets getInsets() {
        return new Insets(10, 10, 10, 5);
    }
}
