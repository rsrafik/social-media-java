import java.util.List;

/**
 * User
 * 
 * The User interface
 * 
 * @author Ropan Datta
 * @version
 * 
 */

public interface User {
    /**
     * Retrieves the unique integer id of the user.
     * 
     * @return the integer id of the user
     */
    int getUserId();

    /**
     * Retrieves the username associated with the user.
     * 
     * @return the username associated with the user
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
     * Checks whether the provided password matches with the user's
     * 
     * @param password the password to test
     * @return true if the password matches
     */
    boolean testPassword(String password);

    /**
     * Retrieves the ids of the user's friends.
     * 
     * @return the list of ids of the user's friends
     */
    List<Integer> getFriendIds();

    /**
     * Retrieves the ids of blocked users.
     * 
     * @return the list of ids of the users who are blocked
     */
    List<Integer> getBlockedUserIds();

    /**
     * Retrieves the ids of the posts created by the user.
     * 
     * @return the list of ids of the posts created by the user
     */
    List<Integer> getPostIds();

    Post[] getPosts();

    /**
     * Retrieves the pending friend requests sent to the user.
     * 
     * @return the list of pending friend requests
     */
    List<FriendRequest> getFriendRequests();

    /**
     * Retrieves the number of friends of the user.
     * 
     * @return the number of friends of the user
     */
    int friendCount();

    /**
     * Retrieves the number of users blocked by the user.
     * 
     * @return the number of users blocked by the user
     */
    int blockedUserCount();

    /**
     * Retrieves the number of posts created by the user.
     * 
     * @return the number of posts created by the user
     */
    int postCount();

    /**
     * Retrieves the number of pending friend requests for the user.
     * 
     * @return the number of pending friend requests for the user
     */
    int friendRequestCount();
}
