import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * FoundationDatabase
 *
 * This class manages a database of User objects and their associated Posts. It provides
 * methods to read users from a file, add new users, retrieve all users, and retrieve all posts.
 * Users are stored in a serialized file format and loaded upon instantiation.
 *
 * @author Rachel Rafik, L22
 * @version November 1, 2024
 */
public class FoundationDatabase {
    private static ArrayList<PlatformUser> users;  // List of User objects in the database
    private static ArrayList<PlatformPost> posts; //List of Post objects in the database
    private static final Object gatekeeper = new Object(); // Synchronization lock for thread safety

    /**
     * Constructs a FoundationDatabase instance and initializes the user list.
     */
    public FoundationDatabase() {
        users = new ArrayList<>();
    }

    /**
     * Reads User objects from the file "users.dat" and populates the users list.
     * If the file does not exist, a message is printed indicating so.
     */
    public void readUsers() {
        try (FileInputStream fileIn = new FileInputStream("users.dat");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            while (true) {
                try {
                    PlatformUser user = (PlatformUser) in.readObject();
                    users.add(user);
                } catch (EOFException e) {
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Does not exist yet"); // File not found or no users saved yet
        }
    }

    /**
     * Adds a new User object to the database, appending it to the "users.dat" file.
     * Synchronization ensures thread-safe access.
     *
     * @param user The User object to be added to the database
     * @throws InterruptedException if thread synchronization is interrupted
     */
    public void addUser(PlatformUser user) throws InterruptedException {
        synchronized (gatekeeper) {
            users.add(user);

            boolean append = new File("users.dat").exists(); // Check if file exists to determine append mode

            try (FileOutputStream fileOut = new FileOutputStream("users.dat", true);
                 ObjectOutputStream out = append ? new AppendableObjectOutputStream(fileOut) : new ObjectOutputStream(fileOut)) {
                out.writeObject(user); // Serialize and write User to file
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error writing file"); // Error during file output
            }
        }
    }

    /**
     * Retrieves all User objects in the database as an array.
     *
     * @return An array of User objects in the database
     */
    public ArrayList<PlatformUser> getAllUsers() {
        return users;
    }

    /**
     * Retrieves all Post objects across all User objects in the database as an array.
     *
     * @return An array of all Post objects associated with users in the database
     */
    public ArrayList<PlatformPost> getAllPosts() {
        ArrayList<PlatformPost> allPosts = new ArrayList<>(); // List to hold all posts
        for (PlatformUser user : users) {
            ArrayList<PlatformPost> userPosts = user.getPosts();
            allPosts.addAll(userPosts); // Add each user's posts to list
        }

        return allPosts;
    }

    /**
     * AppendableObjectOutputStream
     *
     * Custom ObjectOutputStream that avoids writing a new header when appending
     * objects to an existing file. This helps prevent file corruption due to
     * multiple headers in the same file.
     */
    private static class AppendableObjectOutputStream extends ObjectOutputStream {

        /**
         * Constructs an AppendableObjectOutputStream.
         *
         * @param out The OutputStream to write to
         * @throws IOException if an I/O error occurs
         */
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        /**
         * Overrides writeStreamHeader to prevent writing a new header for appending.
         *
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void writeStreamHeader() throws IOException {
            reset(); // Avoids writing a new header when appending
        }
    }
}
