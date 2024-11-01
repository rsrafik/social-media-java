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

    void addUpvote(int userId);

    void addDownvote(int userId);

    default int getScore() {
        return upvoteCounter() - downvoteCounter();
    }
}
