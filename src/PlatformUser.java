import java.io.*;
import java.util.ArrayList;

/**
 * PlatformUser
 *
 * This class represents a user on the platform with unique user ID, username, password,
 * and lists to store posts, friends, blocked users, and friend requests. It allows
 * management of friends and blocked users, and implements the User interface.
 * This class is serializable to allow persistence.
 *
 * @author Rachel Rafik
 * @version November 1, 2024
 */
public class PlatformUser implements User, Serializable {
    //FIELDS
    private static Integer userCount = 0; // Tracks the total number of users on the platform

    private Integer userId; // Unique identifier for the user
    private String username; // Username of the user
    private String password; // Password of the user
    private ArrayList<PlatformPost> posts; // List of posts created by the user

    private ArrayList<Integer> friendIds; // List of friend user IDs
    private ArrayList<Integer> blockedUserIds; // List of blocked user IDs
    private ArrayList<FriendRequest> friendRequests; // List of friend requests received by the user

    //CONSTRUCTORS

    /**
     * Constructs a PlatformUser with the specified username and password.
     * Assigns a unique user ID and increments the user count.
     *
     * @param username The username of the user
     * @param password The password of the user
     */
    public PlatformUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.userId = userCount;
        userCount++;
        posts = new ArrayList<>();
        friendIds = new ArrayList<>();
        blockedUserIds = new ArrayList<>();
        friendRequests = new ArrayList<>();
    }

    //GETTERS/SETTERS

    /**
     * Retrieves the user's unique ID.
     *
     * @return The user's ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The new username to be set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The new password to be set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves an array of posts created by the user.
     *
     * @return An array of the user's posts
     */
    public PlatformPost[] getPosts() {
        return posts.toArray(new PlatformPost[0]);
    }

    /**
     * Retrieves the list of friend IDs.
     *
     * @return A list of user IDs representing the user's friends
     */
    public ArrayList<Integer> getFriendIds() {
        return friendIds;
    }

    /**
     * Retrieves the list of blocked user IDs.
     *
     * @return A list of user IDs representing blocked users
     */
    public ArrayList<Integer> getBlockedUserIds() {
        return blockedUserIds;
    }

    /**
     * Retrieves the list of friend requests received by the user.
     *
     * @return A list of FriendRequest objects
     */
    public ArrayList<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    //METHODS

    /**
     * Retrieves the count of friends the user has.
     *
     * @return The number of friends
     */
    public int friendCount() {
        return friendIds.size();
    }

    /**
     * Retrieves the count of blocked users.
     *
     * @return The number of blocked users
     */
    public int blockedCount() {
        return blockedUserIds.size();
    }

    /**
     * Retrieves the count of friend requests received by the user.
     *
     * @return The number of friend requests
     */
    public int friendRequestCount() {
        return friendRequests.size();
    }
}
