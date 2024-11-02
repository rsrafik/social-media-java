import java.util.List;

/**
 * Comment
 * 
 * The Comment interface
 * 
 * @author Ropan Datta
 * @version
 * 
 */

public interface Comment {
    /**
     * Retrieves the id of the user who created the comment.
     * 
     * @return the id of the user who created the comment
     */
    int getCreatorId();

    /**
     * Retrieves the id of the post underneath which the comment was created.
     * 
     * @return the id of the comment's post
     */
    int getPostId();

    /**
     * Retrieves the number of users who upvoted the comment.
     * 
     * @return the number of users who upvoted the comment.
     */
    int upvoteCounter();

    /**
     * Retrieves the number of users who downvoted the comment.
     * 
     * @return the number of users who downvoted the comment.
     */
    int downvoteCounter();

    /**
     * Retrieves the ids of the users who upvoted the comment.
     * 
     * @return the list of ids of the users who upvoted the comment
     */
    List<Integer> getUpvoterIds();

    /**
     * Retrieves the ids of the users who downvoted the comment.
     * 
     * @return the list of ids of the users who downvoted the comment
     */
    List<Integer> getDownvoterIds();

    /**
     * Attempts to add an upvote to the comment.
     * 
     * @param userId the id of the user attempting to upvote the comment
     * @return true if the user's upvote was added for the first time
     */
    void addUpvote(int userId);

    /**
     * Attempts to add a downvote to the comment.
     * 
     * @param userId the id of the user attempting to downvote the comment
     * @return true if the user's downvote was added for the first time
     */
    void addDownvote(int userId);

    /**
     * Retrieves the 'score' of the comment, i.e., the difference between the upvotes and downvotes.
     * 
     * @return the difference between upvotes and downvotes
     */
    default int calculateScore() {
        return upvoteCounter() - downvoteCounter();
    }
}
