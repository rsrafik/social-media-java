import java.io.*;
import java.util.*;
import java.awt.Image;

/**
 * PlatformUser
 *
 * This class represents a user on the platform with unique user ID, username, password, and lists
 * to store posts, friends, blocked users, and friend requests.
 * 
 * This class is serializable to allow persistence.
 * 
 * @author Rachel Rafik, L22
 * @author Ropan Datta, L22
 * @version December 3, 2024
 */
public class PlatformUser implements User, Serializable {
    private Integer id; // Unique identifier for the user (non-static)
    private String username; // Username of the user
    private String password; // Password of the user
    private Image displayImage; // profile picture
    private ArrayList<Integer> postIds; // List of posts created by the user
    private ArrayList<Integer> friendIds; // List of friend user IDs
    private ArrayList<Integer> blockedUserIds; // List of blocked user IDs
    private ArrayList<Integer> friendRequests; // List of friend requests

    /**
     * Constructs a new PlatformUser with an integer id, username, and password.
     * 
     * @param id the integer ID of the created user
     * @param username the username of the created user
     * @param password the password of the created user
     */
    public PlatformUser(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;

        postIds = new ArrayList<>();
        friendIds = new ArrayList<>();
        blockedUserIds = new ArrayList<>();
        friendRequests = new ArrayList<>();
    }

    @Override
    public Integer getId() {
        return id;
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
    public boolean passwordEquals(String password) {
        return this.password.equals(password);
    }

    @Override
    public Image getDisplayImage() {
        return displayImage;
    }

    @Override
    public void setDisplayImage(Image displayImage) {
        this.displayImage = displayImage;
    }

    @Override
    public void removeDisplayImage() {
        displayImage = null;
    }

    @Override
    public List<Integer> getPostIds() {
        return postIds;
    }

    @Override
    public void addPost(int postId) {
        postIds.add(postId);
    }

    @Override
    public int postCount() {
        return postIds.size();
    }

    @Override
    public List<Integer> getFriendIds() {
        return friendIds;
    }

    @Override
    public boolean addFriend(int userId) {
        if (friendIds.contains(userId)) {
            return true;
        }
        if (!blockedUserIds.contains(userId)) {
            friendIds.add(userId);
            return true;
        }
        return false;
    }

    @Override
    public void removeFriend(int userId) {
        friendIds.remove((Integer) userId);
    }

    @Override
    public int friendCount() {
        return friendIds.size();
    }

    @Override
    public List<Integer> getFriendRequests() {
        return friendRequests;
    }

    @Override
    public boolean addFriendRequest(int userId) {
        if (blockedUserIds.contains(userId)) {
            return false;
        }
        if (!friendRequests.contains(userId)) {
            friendRequests.add(userId);
        }
        return true;
    }

    @Override
    public void removeFriendRequest(int userId) {
        friendRequests.remove((Integer) userId);
    }

    @Override
    public List<Integer> getBlockedUserIds() {
        return blockedUserIds;
    }

    @Override
    public boolean hasBlockedUser(int userId) {
        return blockedUserIds.contains(userId);
    }

    @Override
    public void addBlockedUser(int userId) {
        if (!blockedUserIds.contains(userId)) {
            blockedUserIds.add(userId);
        }
    }

    @Override
    public void removeBlockedUser(int userId) {
        blockedUserIds.remove((Integer) userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlatformUser user) {
            return this.id == user.getId();
        }
        return false;
    }
}
