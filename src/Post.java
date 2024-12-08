import java.util.List;
import java.awt.Image;

/**
 * Post
 * <p>
 * This interface defines the structure for a post on a platform, including methods for managing
 * upvotes, downvotes, comments, and retrieving the content of the post.
 * </p>
 *
 * @author Ropan Datta, L22
 * @version November 15, 2024
 */
public interface Post {

    /**
     * Retrieves the unique integer ID of the post.
     *
     * @return the ID of the post
     */
    Integer getId();

    /**
     * Retrieves the id of the user who created the post.
     * 
     * @return the creator id
     */
    Integer getCreatorId();

    /**
     * Retrieves the content of the post.
     *
     * @return the text content of the post
     */
    String getContent();

    /**
     * Checks whether the post contains an image.
     *
     * @return true if the post contains an image, false otherwise
     */
    boolean hasImage();

    /**
     * Retrieves the image in the post.
     *
     * @return the image in the post, and null if there is no such image
     */
    Image getImage();

    /**
     * Retrieves the IDs of the users who upvoted the post.
     *
     * @return the list of user IDs who upvoted the post
     */
    List<Integer> getUpvoteIds();

    /**
     * Retrieves the IDs of the users who downvoted the post.
     *
     * @return the list of user IDs who downvoted the post
     */
    List<Integer> getDownvoteIds();

    /**
     * Adds an upvote to the post from a specified user.
     *
     * @param userId the ID of the user upvoting the post
     * @return true if the user's upvote was added successfully, false if the user had already
     *         upvoted
     */
    boolean addUpvote(int userId);

    /**
     * Adds a downvote to the post from a specified user.
     *
     * @param userId the ID of the user downvoting the post
     * @return true if the user's downvote was added successfully, false if the user had already
     *         downvoted
     */
    boolean addDownvote(int userId);

    /**
     * Removes an upvote from the post from a specified user.
     *
     * @param userId the ID of the user removing their upvote
     * @return true if the user's upvote was removed successfully, false if the user had not upvoted
     *         before
     */
    boolean removeUpvote(int userId);

    /**
     * Removes a downvote from the post from a specified user.
     *
     * @param userId the ID of the user removing their downvote
     * @return true if the user's downvote was removed successfully, false if the user had not
     *         downvoted before
     */
    boolean removeDownvote(int userId);

    /**
     * Retrieves the number of users who upvoted the post.
     *
     * @return the number of upvotes for the post
     */
    int upvoteCounter();

    /**
     * Retrieves the number of users who downvoted the post.
     *
     * @return the number of downvotes for the post
     */
    int downvoteCounter();

    /**
     * Retrieves the comments made on the post.
     *
     * @return the list of comments associated with the post
     */
    List<Comment> getComments();

    /**
     * Adds a comment to the post.
     *
     * @param comment the comment to add to the post
     */
    void addComment(Comment comment);

    /**
     * Removes a comment from the post.
     * 
     * @param commentId the ID of the comment to remove
     */
    void removeComment(int commentId);

    /**
     * Calculates and returns the score of the post, defined as the difference between the number of
     * upvotes and downvotes.
     *
     * @return the score of the post
     */
    default int calculateScore() {
        return upvoteCounter() - downvoteCounter();
    }
}
