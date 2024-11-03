import java.util.ArrayList;

/**
 * Interface for FoundationDatabase.
 *
 * @author Mckinley Newman
 * @version November 3, 2024
 */
public interface DatabaseInterface {
    void readUsers();
    void readPosts();
    void addUser(PlatformUser user) throws InterruptedException;
    void addPost(PlatformPost post) throws InterruptedException;
    ArrayList<PlatformUser> getAllUsers();
    ArrayList<PlatformPost> getAllPosts();
}
