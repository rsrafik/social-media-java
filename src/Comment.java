import java.util.List;

/**
 * Comment
 * 
 * @author Ropan Datta
 * @version
 * 
 */

public interface Comment {
    int getOwnerId();

    int getPostId();

    int getUpvoteCount();

    int getDownvoteCount();

    List<Integer> getUpvoterIds();

    List<Integer> getDownvoterIds();

    void addUpvote(int userId);

    void addDownvote(int userId);

    default int getScore() {
        return getUpvoteCount() - getDownvoteCount();
    }
}
