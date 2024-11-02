import java.util.List;

/**
 * Comment
 * <p>
 * This interface defines the structure for a comment in a platform, including methods
 * for retrieving the creator ID, managing upvotes and downvotes, and calculating the
 * comment's score.
 * </p>
 *
 * @author Ropan Datta, L22
 * @version November 1, 2024
 */
public interface Comment {

    /**
     * Retrieves the ID of the user who created the comment.
     *
     * @return The ID of the user who created the comment
     */
    int getCreatorId();

    /**
     * Retrieves the number of users who upvoted the comment.
     *
     * @return The number of upvotes for the comment
     */
    int upvoteCounter();

    /**
     * Retrieves the number of users who downvoted the comment.
     *
     * @return The number of downvotes for the comment
     */
    int downvoteCounter();

    /**
     * Retrieves the IDs of users who upvoted the comment.
     *
     * @return A list of user IDs who upvoted the comment
     */
    List<Integer> getUpvoteIds();

    /**
     * Retrieves the IDs of users who downvoted the comment.
     *
     * @return A list of user IDs who downvoted the comment
     */
    List<Integer> getDownvoteIds();

    /**
     * Adds an upvote to the comment from a specified user.
     *
     * @param userId The ID of the user upvoting the comment
     */
    void addUpvote(int userId);

    /**
     * Adds a downvote to the comment from a specified user.
     *
     * @param userId The ID of the user downvoting the comment
     */
    void addDownvote(int userId);

    /**
     * Calculates and returns the score of the comment, defined as the difference
     * between the number of upvotes and downvotes.
     *
     * @return The score of the comment
     */
    default int calculateScore() {
        return upvoteCounter() - downvoteCounter();
    }
}
