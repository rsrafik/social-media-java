import java.util.List;

/**
 * Post
 * 
 * @author Ropan Datta
 * @version
 * 
 */

public interface Post {
    int getOwnerId();

    int getUpvoteCount();

    int getDownvoteCount();

    List<Integer> getUpvoterIds();

    List<Integer> getDownvoterIds();

    List<Comment> getComments();

    String getTitle();

    String getBody();

    void addUpvote(int userId);

    void addDownvote(int userId);

    default int getScore() {
        return getUpvoteCount() - getDownvoteCount();
    }
}
