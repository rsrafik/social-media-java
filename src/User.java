import java.util.List;

/**
 * User
 * 
 * @author Ropan Datta
 * @version
 * 
 */

public interface User {
    int getId();

    String getUsername();

    void setUsername(String username);

    void setPassword(String password);

    List<Integer> getFriendIds();

    List<Integer> getBlockedUserIds();

    List<Integer> getPostIds();

    List<Integer> getCommentIds();

    List<FriendRequest> getFriendRequests();
}
