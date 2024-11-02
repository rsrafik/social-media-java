import java.util.List;

/**
 * Comment
 * 
 * @author Ropan Datta
 * @version
 * 
 */

public interface Comment {
    int getCreatorId();

    int upvoteCounter();

    int downvoteCounter();

    List<Integer> getUpvoterIds();

    List<Integer> getDownvoterIds();

    void addUpvote(int userId);

    void addDownvote(int userId);

    default int getScore() {
        return upvoteCounter() - downvoteCounter();
    }
}
