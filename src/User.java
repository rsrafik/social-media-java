import java.util.List;

/**
 * User
 * <p>
 * This interface defines the structure for a user on a platform, including methods
 * for managing the user's friends, blocked users, posts, and friend requests.
 * It provides methods for retrieving user information, updating usernames and passwords,
 * and verifying login credentials.
 * </p>
 *
 * @author Ropan Datta, L22
 * @version November 1, 2024
 */
public interface User {

    /**
     * Retrieves the unique ID of the user.
     *
     * @return The user's ID
     */
    int getUserId();

    /**
     * Retrieves the username associated with the user.
     *
     * @return The username
     */
    String getUsername();

    /**
     * Sets the username of the user.
     *
     * @param username The username to set
     */
    void setUsername(String username);

    /**
     * Sets the password of the user.
     *
     * @param password The password to set
     */
    void setPassword(String password);

    /**
     * Checks whether the provided password matches the user's password.
     *
     * @param password The password to test
     * @return true if the password matches, false otherwise
     */
    boolean testPassword(String password);

    /**
     * Checks whether the provided username matches the user's username.
     *
     * @param username The username to test
     * @return true if the username matches, false otherwise
     */
    boolean testUsername(String username);

    /**
     * Retrieves the IDs of the user's friends.
     *
     * @return A list of user IDs representing the user's friends
     */
    List<Integer> getFriendIds();

    /**
     * Retrieves the IDs of users blocked by the user.
     *
     * @return A list of user IDs representing blocked users
     */
    List<Integer> getBlockedUserIds();

    /**
     * Retrieves the IDs of posts created by the user.
     *
     * @return A list of post IDs created by the user
     */
    List<Integer> getPostIds();

    /**
     * Retrieves an array of posts created by the user.
     *
     * @return An array of the user's posts
     */
    List<? extends Post> getPosts();

    /**
     * Retrieves the pending friend requests sent to the user.
     *
     * @return A list of pending friend requests
     */
    List<? extends FriendRequest> getFriendRequests();

    /**
     * Retrieves the number of friends the user has.
     *
     * @return The number of friends
     */
    int friendCount();

    /**
     * Retrieves the number of users blocked by the user.
     *
     * @return The number of blocked users
     */
    int blockedUserCount();

    /**
     * Retrieves the number of posts created by the user.
     *
     * @return The number of posts created by the user
     */
    int postCount();

    /**
     * Retrieves the number of pending friend requests for the user.
     *
     * @return The number of pending friend requests
     */
    int friendRequestCount();
}
