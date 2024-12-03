import java.util.List;
import java.awt.Image;

/**
 * User
 * <p>
 * This interface defines the structure for a user on a platform, including methods for managing the
 * user's friends, blocked users, posts, and friend requests. It provides methods for retrieving
 * user information, updating usernames and passwords, and verifying login credentials.
 * </p>
 *
 * @author Ropan Datta, L22
 * @version November 1, 2024
 */
public interface User {

    /**
     * Retrieves the unique ID of the user.
     *
     * @return the ID of the user
     */
    Integer getId();

    /**
     * Retrieves the username associated with the user.
     *
     * @return the username
     */
    String getUsername();

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    void setUsername(String username);

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    void setPassword(String password);

    /**
     * Checks whether the provided password matches the user's password.
     *
     * @param password The password to test for equality
     * @return true if the password matches, false otherwise
     */
    boolean passwordEquals(String password);

    /**
     * Retrieves the user's profile picture.
     * 
     * @return the profile picture of the user
     */
    Image getDisplayImage();

    /**
     * Sets a new profile picture for the user.
     * 
     * @param image the new profile picture
     */
    void setDisplayImage(Image image);

    /**
     * Removes the user's profile picture.
     */
    void removeDisplayImage();

    /**
     * Retrieves the IDs of posts created by the user.
     *
     * @return the list of IDs of posts created by the user
     */
    List<Integer> getPostIds();

    /**
     * Adds a post.
     * 
     * @param postId the ID of the post
     */
    void addPost(int postId);

    /**
     * Retrieves the number of posts created by the user.
     *
     * @return The number of posts created by the user
     */
    int postCount();

    /**
     * Retrieves the IDs of users blocked by the user.
     *
     * @return the list of IDs of blocked users
     */
    List<Integer> getBlockedUserIds();

    /**
     * Checks whether or not a user has been blocked.
     * 
     * @param userId the ID of the user to check for
     * @return whether or not the user has been blocked
     */
    boolean hasBlockedUser(int userId);

    /**
     * Blocks a user.
     * 
     * @param userId the ID of the user to block
     */
    void addBlockedUser(int userId);

    /**
     * Unblocks a user.
     * 
     * @param userId the ID of the user to unblock
     */
    void removeBlockedUser(int userId);

    /**
     * Retrieves the IDs of the user's friends.
     *
     * @return the list of user IDs representing the user's friends
     */
    List<Integer> getFriendIds();

    /**
     * Adds a friend.
     * 
     * @param userId the ID of the user to add as a friend
     * @return {@code true} if the user was added or was already present in the list of friends,
     *         {@code false} if the user is blocked or could not be added for some reason
     */
    boolean addFriend(int userId);

    /**
     * Removes a friend.
     * 
     * @param userId the ID of the friend to remove
     */
    void removeFriend(int userId);

    /**
     * Retrieves the number of friends the user has.
     *
     * @return the number of friends of the user
     */
    int friendCount();

    /**
     * Retrieves the pending friend requests sent to the user.
     *
     * @return A list of pending friend requests
     */
    List<Integer> getFriendRequests();

    /**
     * Adds an incoming friend request to the user.
     * 
     * @param userId the ID of the user who sent the friend request
     * @return {@code true} if the friend request was sent successfully, {@code false} if the friend
     *         request failed
     */
    boolean addFriendRequest(int userId);

    /**
     * Removes an incoming friend request to the user.
     * 
     * @param userId the ID of the user who canceled the friend request
     */
    void removeFriendRequest(int userId);
}
