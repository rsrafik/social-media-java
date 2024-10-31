import java.util.ArrayList;

public class PlatformUser {
    //FIELDS
    private static Integer userCount = 0; //How many users the application has

    private Integer userId; //User ID
    private String username; //User username
    private String password; //User password
    private Post[] posts; //Stores the list a posts made by current User

    private ArrayList<Integer> friendIds; //Stores the list of friends
    private ArrayList<Integer> blockedUserIds; //Stores the list of blocked users
    private ArrayList<FriendRequest> friendRequests; //Stores the list a posts made by current User

    //CONSTRUCTORS
    public PlatformUser(String username, String password) {
        this.username = username;
        this.password = password;
        userId = userCount;
        userCount++;
    }

    //GETTERS/SETTERS
    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Post[] getPosts() {
        return posts;
    }

    public ArrayList<Integer> getFriendIds() {
        return friendIds;
    }

    public ArrayList<Integer> getBlockedUserIds() {
        return blockedUserIds;
    }

    /**
     * @return
     */
    public ArrayList<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    //METHODS

    /**
     * @return Returns number of friends a user has
     */
    public int friendCount() {
        return friendIds.size();
    }

    /**
     * @return Returns number of blocked users a user has
     */
    public int blockedCount() {
        return blockedUserIds.size();
    }

    /**
     * @return Returns number of friends requests a user has
     */
    public int friendRequestCount() {
        return friendRequests.size();
    }
}
