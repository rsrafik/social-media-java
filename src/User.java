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
     * Checks whether the user has a profile picture.
     *
     * @return whether or not the user's display image is null
     */
    boolean hasDisplayImage();

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
     * Retrieves the IDs of the users being followed.
     *
     * @return the the list of IDs the user is following
     */
    List<Integer> getFollowingIds();

    /**
     * Follows a user.
     *
     * @param userId the ID of the user to follow
     */
    void addFollowing(int userId);

    /**
     * Unfollows a user.
     *
     * @param userId the ID of the user to unfollow
     */
    void removeFollowing(int userId);

    /**
     * Retrieves the IDs of the user's followers.
     *
     * @return the list of user IDs representing the user's followers
     */
    List<Integer> getFollowerIds();

    /**
     * Adds a follower.
     *
     * @param userId the ID of the user to add as a follower
     * @return {@code true} if the user was added or was already present in the list of followers,
     * {@code false} if the user is blocked or could not be added for some reason
     */
    boolean addFollower(int userId);

    /**
     * Removes a follower.
     *
     * @param userId the ID of the follower to remove
     */
    void removeFollower(int userId);

    /**
     * Retrieves the number of follower the user has.
     *
     * @return the number of followers of the user
     */
    int followerCount();

    /**
     * Retrieves the pending follow requests sent to the user.
     *
     * @return A list of pending follow requests
     */
    List<Integer> getFollowRequests();

    /**
     * Adds an incoming follow request to the user.
     *
     * @param userId the ID of the user who sent the follow request
     * @return {@code true} if the follow request was sent successfully, {@code false} if the follow
     * request failed
     */
    boolean addFollowRequest(int userId);

    /**
     * Removes an incoming follow request to the user.
     *
     * @param userId the ID of the user who canceled the follow request
     */
    void removeFollowRequest(int userId);

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
}
