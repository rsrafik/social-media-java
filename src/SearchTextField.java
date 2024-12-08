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
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchTextField extends JTextField {
    public static UserInfo chosenOne;

    private final TransparentJButton searchButton;
    private final FontIcon searchIcon;

    // Interface for handling search actions
    public interface SearchActionListener {
        void onSearch(String selectedOption);
    }

    private SearchActionListener searchActionListener;

    public SearchTextField() {
        super(); // Call the constructor of JTextField

        // Set up placeholder text
        setText("Search here...");
        setForeground(Color.GRAY);

        // Add focus listener to manage placeholder behavior
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

        // Create FontAwesome search icon
        searchIcon = FontIcon.of(FontAwesome.SEARCH, 16);

        // Create the search button
        searchButton = new TransparentJButton(""); // No text, only icon
        searchButton.setIcon(searchIcon); // Set the search icon
        searchButton.setPreferredSize(new Dimension(30, 30)); // Set button size
        searchButton.setEnabled(false); // Initially disable the button

        // Add action listener to the search button
        searchButton.addActionListener(e -> {
            try {
                handleSearchAction();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Add a DocumentListener to the text field to enable/disable the search button
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

        // Style the search field
        setBackground(new Color(230, 230, 230)); // Light gray background
        setBorder(new EmptyBorder(5, 10, 5, 40)); // Add padding inside
        setPreferredSize(new Dimension(200, 30)); // Set field size

        // Set up rounded edges
        setOpaque(false);

        // Add the button to the field's parent container
        setLayout(new BorderLayout());
        add(searchButton, BorderLayout.EAST);
    }

    public void setSearchActionListener(SearchActionListener listener) {
        this.searchActionListener = listener;
    }

    private void handleSearchAction() throws IOException, ClassNotFoundException {
        // Get the current text in the search field
        String searchText = getText();

        List<UserInfo> results = PlatformRunner.client.searchUser(searchText);
        List<String> usernames = new ArrayList<>();


        for(int i = 0; i < results.size(); i++) {
            if (results.get(i).getId().equals(PlatformRunner.client.fetchLoggedInUser().getId()))
                results.remove(i);
        }

        for(UserInfo result: results) {
                usernames.add(result.getUsername());
        }



        // Create a dropdown with options
        JComboBox<String> dropdown = new JComboBox<>(usernames.toArray(new String[0]));

        // Prepare the message for the dialog
        Object[] message = {
                "\nSearch for: " + searchText,
                "Select a user:",
                dropdown,
                "\n"
        };

        // Show a JOptionPane with the dropdown and custom buttons
        int result = JOptionPane.showOptionDialog(
                null,
                message,
                "Search",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Search", "Cancel"},
                "Search" // Default button
        );

        // If "Search" is clicked, trigger the listener
        if (result == JOptionPane.YES_OPTION && searchActionListener != null) {
            String selectedOption = (String) dropdown.getSelectedItem();
            UserInfo selectedUser = null;

            for(int i = 0; i < usernames.size(); i++) {
                assert selectedOption != null;
                if (selectedOption.equals(usernames.get(i))) {
                    selectedUser = results.get(i);
                    break;
                }
            }

            chosenOne = selectedUser;
            searchActionListener.onSearch(selectedOption);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Paint rounded rectangle for the text field background
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded corners
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border to paint
    }

    @Override
    public void setBorder(Border border) {
        // Ignore default border setting
    }

    @Override
    public Insets getInsets() {
        // Provide custom insets for padding inside the text field
        return new Insets(10, 10, 10, 5); // Top, Left, Bottom, Right
    }

    public TransparentJButton getSearchButton() {
        return searchButton;
    }
}
