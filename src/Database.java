import java.io.IOException;
import java.util.List;

/**
 * Interface for FoundationDatabase, defining methods to manage users and posts in a persistent
 * storage system.
 *
 * @author Mckinley Newman, L22
 * @author Ropan Datta, L22
 * @version November 15, 2024
 */
public interface Database {

    /**
     * Reads users from a file on disk
     * 
     * @param filename the file to read from
     * @throws IOException if a file I/O error occurs
     * @throws ClassNotFoundException if there is bad data in the file
     */
    void readUsers(String filename) throws IOException, ClassNotFoundException;

    /**
     * Reads posts from a file on disk
     * 
     * @param filename the file to read from
     * @throws IOException if a file I/O error occurs
     * @throws ClassNotFoundException if there is bad data in the file
     */
    void readPosts(String filename) throws IOException, ClassNotFoundException;

    /**
     * Retrieves all users stored in the database.
     *
     * @return the list of users in the database
     */
    List<User> getUsers();

    /**
     * Retrieves the unique user associated with an integer ID.
     * 
     * @param userId the unique ID of the user
     * @return the user associated with the integer ID
     */
    User getUser(int userId);

    /**
     * Retrieves the unique integer ID associated with a username.
     * 
     * @param username the username to query
     * @return the unique integer ID associated witht the username or null if the user doesn't exist
     */
    Integer getUserId(String username);

    /**
     * Adds a new user to the database.
     *
     * @param user the User to add to the database
     */
    void addUser(User user);

    /**
     * Retrieves the number of users in the database.
     * 
     * @return the total number of users
     */
    int userCount();

    /**
     * Retrieves all posts stored in the database.
     *
     * @return A LinkedHashMap of PlatformPost objects with post IDs as keys.
     */
    List<Post> getPosts();

    /**
     * Retrieves the post associated with a post ID.
     * 
     * @param postId the unique ID of the post
     * @return the post associated with the integer ID
     */
    Post getPost(int postId);

    /**
     * Adds a new post to the database.
     *
     * @param post The PlatformPost to add to the database
     */
    void addPost(Post post);

    /**
     * Saves user information to disk.
     * 
     * @param filename the file to save to
     * @throws IOException if a file I/O error occurs
     */
    void saveUsers(String filename) throws IOException;

    /**
     * Saves post information to disk.
     * 
     * @param filename the file to save to
     * @throws IOException if a file I/O error occurs
     */
    void savePosts(String filename) throws IOException;
}
