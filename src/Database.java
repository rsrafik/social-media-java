import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
     */
    void addUser(PlatformUser user);

    /**
     * Adds a new post to the database and saves it to persistent storage.
     *
     * @param post The PlatformPost to add to the database
     */
    void addPost(PlatformPost post);

    /**
     * Retrieves all users stored in the database.
     *
     * @return A LinkedHashMap of all PlatformUser objects with usernames as keys.
     */
    LinkedHashMap<String, PlatformUser> getAllUsers();

    /**
     * Retrieves all posts stored in the database.
     *
     * @return A LinkedHashMap of all PlatformPost objects with post IDs as keys.
     */
    LinkedHashMap<Integer, PlatformPost> getAllPosts();
}
