import java.util.List;

/**
 * Post
 * 
 * @author Ropan
 * @version
 * 
 */

public interface Post {
    int getOwnerId();

    List<Integer> getUpvoteIds();

    List<Integer> getDownvoteIds();

    int getUpvoteCount();

    int getDownvoteCount();

    default int getScore() {
        return getUpvoteCount() - getDownvoteCount();
    }

    List<Comment> getComments();

    String getTitle();

    String getBody();

    void addUpvote(int userId);

    void addDownvote(int userId);
}
