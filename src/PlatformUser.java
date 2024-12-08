import java.io.*;
import java.util.*;
import java.awt.Image;

/**
 * PlatformUser
 * <p>
 * This class represents a user on the platform with unique user ID, username, password, and lists
 * to store posts, friends, blocked users, and friend requests.
 * <p>
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
    private Image displayImage; // Profile picture of the user
    private ArrayList<Integer> postIds; // List of posts created by the user
    private ArrayList<Integer> followingIds; // List of IDs being followed
    private ArrayList<Integer> followerIds; // List of follower IDs
    private ArrayList<Integer> blockedUserIds; // List of blocked user IDs
    private ArrayList<Integer> followRequests; // List of follow requests

    /**
     * Constructs a new PlatformUser with an integer ID, username, and password.
     *
     * @param id       the integer ID of the created user
     * @param username the username of the created user
     * @param password the password of the created user
     */
    public PlatformUser(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;

        postIds = new ArrayList<>();
        followingIds = new ArrayList<>();
        followerIds = new ArrayList<>();
        blockedUserIds = new ArrayList<>();
        followRequests = new ArrayList<>();
    }

    /**
     * Copy constructor.
     *
     * @param user the PlatformUser to copy
     */
    public PlatformUser(PlatformUser user) {
        id = user.id;
        username = user.username;
        password = user.password;

        postIds = new ArrayList<>(user.postIds);
        followingIds = new ArrayList<>(user.followingIds);
        followerIds = new ArrayList<>(user.followerIds);
        blockedUserIds = new ArrayList<>(user.blockedUserIds);
        followRequests = new ArrayList<>(user.followRequests);
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
    public boolean hasDisplayImage() {
        return displayImage != null;
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
    public List<Integer> getFollowingIds() {
        return followingIds;
    }

    @Override
    public void addFollowing(int userId) {
        if (!followingIds.contains(userId)) {
            followingIds.add(userId);
        }
    }

    @Override
    public void removeFollowing(int userId) {
        followingIds.remove((Integer) userId);
    }

    @Override
    public List<Integer> getFollowerIds() {
        return followerIds;
    }

    @Override
    public boolean addFollower(int userId) {
        if (blockedUserIds.contains(userId)) {
            return false;
        }
        if (!followerIds.contains(userId)) {
            followerIds.add(userId);
        }
        return true;
    }

    @Override
    public void removeFollower(int userId) {
        followerIds.remove((Integer) userId);
    }

    @Override
    public int followerCount() {
        return followerIds.size();
    }

    @Override
    public List<Integer> getFollowRequests() {
        return followRequests;
    }

    @Override
    public boolean addFollowRequest(int userId) {
        if (blockedUserIds.contains(userId)) {
            return false;
        }
        if (!followRequests.contains(userId)) {
            followRequests.add(userId);
        }
        return true;
    }

    @Override
    public void removeFollowRequest(int userId) {
        followRequests.remove((Integer) userId);
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
            removeFollowRequest(userId);
            removeFollower(userId);
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
