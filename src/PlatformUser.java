import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * PlatformUser
 *
 * This class represents a user on the platform with unique user ID, username, password,
 * and lists to store posts, friends, blocked users, and friend requests.
 * This class is serializable to allow persistence.
 */
public class PlatformUser implements User, Serializable {

    // Static Fields
    private static final String USER_COUNT_FILE = "userCount.dat";
    private static Integer userCount = loadUserCount(); // Load userCount from file on startup

    // Instance Fields
    private Integer userId; // Unique identifier for the user (non-static)
    private String username; // Username of the user
    private String password; // Password of the user
    private ArrayList<PlatformPost> posts; // List of posts created by the user
    private ArrayList<PlatformUser> friends; // List of friend user IDs
    private ArrayList<Integer> blockedUserIds; // List of blocked user IDs
    private ArrayList<PlatformFriendRequest> friendRequests; // List of friend requests

    // Constructor
    public PlatformUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.userId = userCount++;
        saveUserCount(); // Save the incremented userCount to the file
        posts = new ArrayList<>();
        friends = new ArrayList<>();
        blockedUserIds = new ArrayList<>();
        friendRequests = new ArrayList<>();
    }

    // Static Method to Load userCount from a File
    private static Integer loadUserCount() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(USER_COUNT_FILE))) {
            return in.readInt();
        } catch (IOException e) {
            // File does not exist or cannot be read, default to 0
            return 0;
        }
    }

    // Static Method to Save userCount to a File
    private static void saveUserCount() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USER_COUNT_FILE))) {
            out.writeInt(userCount);
        } catch (IOException e) {
            System.err.println("Error saving user count: " + e.getMessage());
        }
    }

    // Getter for userId
    @Override
    public int getUserId() {
        return userId;
    }

    // Getter for username
    @Override
    public String getUsername() {
        return username;
    }

    // Setter for username
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    // Setter for password
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for posts
    @Override
    public ArrayList<PlatformPost> getPosts() {
        return posts;
    }

    // Getter for friends
    @Override
    public ArrayList<PlatformUser> getFriends() {
        return friends;
    }

    // Getter for blockedUserIds
    @Override
    public ArrayList<Integer> getBlockedUserIds() {
        return blockedUserIds;
    }

    // Getter for friendRequests
    @Override
    public ArrayList<PlatformFriendRequest> getFriendRequests() {
        return friendRequests;
    }

    // Method to retrieve post IDs
    @Override
    public ArrayList<Integer> getPostIds() {
        ArrayList<Integer> postIds = new ArrayList<>();
        for (PlatformPost post : posts) {
            postIds.add(post.getPostId());
        }
        return postIds;
    }

    // Method to count friends
    @Override
    public int friendCount() {
        return friends.size();
    }

    // Method to count blocked users
    @Override
    public int blockedUserCount() {
        return blockedUserIds.size();
    }

    // Method to count posts
    @Override
    public int postCount() {
        return posts.size();
    }

    // Method to count friend requests
    @Override
    public int friendRequestCount() {
        return friendRequests.size();
    }

    // Method to test if the given password matches the user's password
    @Override
    public boolean testPassword(String testedPassword) {
        return this.password.equals(testedPassword);
    }

    // Method to test if the given username matches the user's username
    @Override
    public boolean testUsername(String testedUsername) {
        return this.username.equals(testedUsername);
    }

    // Equals method to compare PlatformUser objects by userId
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlatformUser) {
            return ((PlatformUser) obj).getUserId() == this.userId;
        }
        return false;
    }

    public boolean answerFriendRequest(PlatformFriendRequest fr, Scanner scanner, FoundationDatabase fd) {
        System.out.println("Do you want to accept the friend request from " + fr.getUser() + "? (yes/no)");
        String response = scanner.nextLine().trim().toLowerCase();

        switch (response) {
            case "yes":
                PlatformUser sender = fr.accept(fd);
                if (sender != null) {
                    friends.add(sender); // Add the sender to the friends list
                    friendRequests.remove(fr); // Remove the friend request after accepting
                    fd.saveUsers(); // Update the database
                    return true;
                } else {
                    return false;
                }
            case "no":
                friendRequests.remove(fr); // Remove the friend request after declining
                fd.saveUsers(); // Update the database
                return false;
            default:
                return false;
        }
    }

}
