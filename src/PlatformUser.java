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
 * @author Ropan Datta
 * @version November 2, 2024
 */
public class PlatformUser implements User, Serializable {
    // STATIC VARIABLES

    private static Integer userCount = 0; // Tracks the total number of users on the platform

    // FIELDS

    private Integer userId; // Unique identifier for the user
    private String username; // Username of the user
    private String password; // Password of the user
    private ArrayList<Integer> postIds; // List of posts created by the user

    private ArrayList<Integer> friendIds; // List of friend user IDs
    private ArrayList<Integer> blockedUserIds; // List of blocked user IDs
    private ArrayList<FriendRequest> friendRequests; // List of friend requests received by the user

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
        postIds = new ArrayList<>();
        friendIds = new ArrayList<>();
        blockedUserIds = new ArrayList<>();
        friendRequests = new ArrayList<>();
    }

    // GETTERS/SETTERS

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean testPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public ArrayList<Integer> getPostIds() {
        return postIds;
    }

    @Override
    public ArrayList<Integer> getFriendIds() {
        return friendIds;
    }

    @Override
    public ArrayList<Integer> getBlockedUserIds() {
        return blockedUserIds;
    }

    @Override
    public ArrayList<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    // METHODS

    @Override
    public int postCount() {
        return postIds.size();
    }

    @Override
    public int friendCount() {
        return friendIds.size();
    }

    @Override
    public int blockedUserCount() {
        return blockedUserIds.size();
    }

    @Override
    public int friendRequestCount() {
        return friendRequests.size();
    }
}
