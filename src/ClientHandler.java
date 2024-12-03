import java.util.List;
import java.awt.Image;

/**
 * ClientHandler
 * 
 * <p>
 * Defines an interface for a client handler.
 * </p>
 * 
 * @author Ropan Datta, L22
 * @version November 16, 2024
 */
public interface ClientHandler extends Runnable {

    /**
     * Checks whether a user is currently logged in.
     * 
     * @return {@code true} if a user is logged in, {@code false} otherwise
     */
    boolean isLoggedIn();

    /**
     * Attempts to log in.
     * 
     * @param username the username to log in with
     * @param password the user's password
     * @return {@code true} if the login is successful, {@code false} if the user does not exist or
     *         if the password is incorrect
     */
    boolean logIn(String username, String password);

    /**
     * Logs out if a user is logged in.
     * 
     * @return {@code true} if the user was logged out, {@code false} if no user was logged in
     */
    boolean logOut();

    /**
     * Creates a new User.
     * 
     * @param username the username of the new user
     * @param password the password of the new user
     * @return {@code true} if the user was created successfully, {@code false} if user creation
     *         failed (e.g. if the username was already taken)
     */
    boolean createUser(String username, String password);

    /**
     * Creates a new post.
     * 
     * @param content the text content of the post
     * @param image an optional image added to the post
     * @return {@code true} if the post was created successfully, {@code false} if post creation
     *         failed
     */
    boolean createPost(String content, Image image);

    /**
     * Retrieves the IDs of the blocked users.
     * 
     * @return the list of blocked user IDs
     */
    List<Integer> getBlockedUserIds();

    /**
     * Adds a user to the list of blocked users.
     * 
     * @param userId the ID of the user to block
     * @return {@code true} if the user was blocked successfully
     */
    boolean addBlockedUser(int userId);

    /**
     * Removes a user from the list of blocked users.
     * 
     * @param userId the ID of the user to unblock
     * @return {@code true} if the user was unblocked successfully
     */
    boolean removeBlockedUser(int userId);

    /**
     * Tries to send a friend request to another user or accepts an incoming friend request.
     * 
     * @param userId the ID of the user to send a friend request to or accept a friend request from
     * @return {@code true} if the friend request was sent or accepted successfully, {@code false}
     *         otherwise
     */
    boolean sendFriendRequest(int userId);

    /**
     * Retrieves a post
     */
    Post fetchPost(int postId);
}
