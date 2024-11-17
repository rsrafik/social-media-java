import java.util.List;

/**
 * Comment
 * <p>
 * This interface defines the structure for a comment in a platform, including methods for
 * retrieving the creator ID, managing upvotes and downvotes, and calculating the comment's score.
 * </p>
 *
 * @author Ropan Datta, L22
 * @version November 15, 2024
 */
public interface Comment {

    /**
     * Retrieves the ID of the user who created the comment.
     *
     * @return the ID of the user who created the comment
     */
    Integer getCreatorId();

    /**
     * Retrieves the content of the comment
     */
    String getContent();

    /**
     * Retrieves the IDs of users who upvoted the comment.
     *
     * @return the list of user IDs who upvoted the comment
     */
    List<Integer> getUpvoteIds();

    /**
     * Retrieves the IDs of users who downvoted the comment.
     *
     * @return the list of user IDs who downvoted the comment
     */
    List<Integer> getDownvoteIds();

    /**
     * Adds an upvote to the comment from a specified user.
     *
     * @param userId the ID of the user upvoting the comment
     */
    void addUpvote(int userId);

    /**
     * Adds a downvote to the comment from a specified user.
     *
     * @param userId the ID of the user downvoting the comment
     */
    void addDownvote(int userId);

    /**
     * Retrieves the number of users who upvoted the comment.
     *
     * @return the number of upvotes for the comment
     */
    int upvoteCounter();

    /**
     * Retrieves the number of users who downvoted the comment.
     *
     * @return the number of downvotes for the comment
     */
    int downvoteCounter();

    /**
     * Calculates and returns the score of the comment, defined as the difference between the number
     * of upvotes and downvotes.
     *
     * @return the score of the comment
     */
    default int calculateScore() {
        return upvoteCounter() - downvoteCounter();
    }
}
