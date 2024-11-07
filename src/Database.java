import java.util.ArrayList;

/**
 * Interface for FoundationDatabase, defining methods to manage users and posts
 * in a persistent storage system.
 *
 * @author Mckinley Newman, L22
 * @version November 3, 2024
 */
public interface Database {

    /**
     * Reads and loads users from a persistent storage into the database.
     */
    void readUsers();

    /**
     * Reads and loads posts from a persistent storage into the database.
     */
    void readPosts();

    /**
     * Adds a new user to the database and saves it to persistent storage.
     *
     * @param user The PlatformUser to add to the database
     * @throws InterruptedException if the thread is interrupted during the operation
     */
    void addUser(PlatformUser user) throws InterruptedException;

    /**
     * Adds a new post to the database and saves it to persistent storage.
     *
     * @param post The PlatformPost to add to the database
     * @throws InterruptedException if the thread is interrupted during the operation
     */
    void addPost(PlatformPost post) throws InterruptedException;

    /**
     * Retrieves all users stored in the database.
     *
     * @return An ArrayList of all PlatformUser objects in the database
     */
    ArrayList<PlatformUser> getAllUsers();

    /**
     * Retrieves all posts stored in the database.
     *
     * @return An ArrayList of all PlatformPost objects in the database
     */
    ArrayList<PlatformPost> getAllPosts();
}
