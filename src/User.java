import java.util.List;

/**
 * User
 * 
 * @author Ropan Datta
 * @version
 * 
 */

public interface User {
    int getUserId();

    String getUsername();

    void setUsername(String username);

    void setPassword(String password);

    List<Integer> getFriendIds();

    List<Integer> getBlockedUserIds();

    Post[] getPosts();

    List<FriendRequest> getFriendRequests();

    int friendCount();

    int blockedCount();

    int friendRequestCount();
}
