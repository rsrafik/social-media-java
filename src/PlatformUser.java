import java.io.*;
import java.util.ArrayList;

/**
 * PlatformUser
 * <p>
 * This class represents a user on the platform with unique user ID, username, password,
 * and lists to store posts, friends, blocked users, and friend requests. It allows
 * management of friends and blocked users, and implements the User interface.
 * This class is serializable to allow persistence.
 * </p>
 *
 * @author Rachel Rafik, L22
 * @version November 1, 2024
 */
public class PlatformUser implements User, Serializable {

    // FIELDS

    private static Integer userCount = 0; // Tracks the total number of users on the platform

    private Integer userId; // Unique identifier for the user
    private String username; // Username of the user
    private String password; // Password of the user
    private ArrayList<PlatformPost> posts; // List of posts created by the user

    private ArrayList<Integer> friendIds; // List of friend user IDs
    private ArrayList<Integer> blockedUserIds; // List of blocked user IDs
    private ArrayList<PlatformFriendRequest> friendRequests; // List of friend requests received by
                                                             // the user

    // CONSTRUCTORS

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

    // GETTERS/SETTERS

    /**
     * Retrieves the user's unique ID.
     *
     * @return The user's ID
     */
    @Override
    public int getUserId() {
        return userId;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The new username to be set
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The new password to be set
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves an array of posts created by the user.
     *
     * @return An array of the user's posts
     */
    @Override
    public ArrayList<PlatformPost> getPosts() {
        return posts;
    }

    /**
     * Retrieves the list of friend IDs.
     *
     * @return A list of user IDs representing the user's friends
     */
    @Override
    public ArrayList<Integer> getFriendIds() {
        return friendIds;
    }

    /**
     * Retrieves the list of blocked user IDs.
     *
     * @return A list of user IDs representing blocked users
     */
    @Override
    public ArrayList<Integer> getBlockedUserIds() {
        return blockedUserIds;
    }

    /**
     * Retrieves the list of friend requests received by the user.
     *
     * @return A list of FriendRequest objects
     */
    @Override
    public ArrayList<PlatformFriendRequest> getFriendRequests() {
        return friendRequests;
    }

    /**
     * Retrieves the list of post IDs created by the user.
     *
     * @return A list of post IDs
     */
    @Override
    public ArrayList<Integer> getPostIds() {
        ArrayList<Integer> postIds = new ArrayList<>();
        for (PlatformPost post : posts) {
            postIds.add(post.getPostId());
        }
        return postIds;
    }

    // METHODS

    /**
     * Retrieves the count of friends the user has.
     *
     * @return The number of friends
     */
    @Override
    public int friendCount() {
        return friendIds.size();
    }

    /**
     * Retrieves the count of blocked users.
     *
     * @return The number of blocked users
     */
    @Override
    public int blockedUserCount() {
        return blockedUserIds.size();
    }

    /**
     * Retrieves the count of posts created by the user.
     *
     * @return The number of posts
     */
    @Override
    public int postCount() {
        return posts.size();
    }

    /**
     * Retrieves the count of friend requests received by the user.
     *
     * @return The number of friend requests
     */
    @Override
    public int friendRequestCount() {
        return friendRequests.size();
    }

    /**
     * Checks if the given password matches the user's password.
     *
     * @param password The password to check
     * @return true if the password matches, false otherwise
     */
    @Override
    public boolean testPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Checks if the given username matches the user's username.
     *
     * @param username The username to check
     * @return true if the username matches, false otherwise
     */
    @Override
    public boolean testUsername(String username) {
        return this.password.equals(username);
    }

    /**
     * Checks if the given object is an instance of Platform User and has the same username and
     * password.
     *
     * @param obj The object to check
     * @return true if the User matches, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlatformUser) {
            return ((PlatformUser) obj).getUserId() == this.userId;
        }
        return false;
    }
}
