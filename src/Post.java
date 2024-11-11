import java.util.List;
import java.awt.Image;

/**
 * Post
 * <p>
 * This interface defines the structure for a post on a platform, including methods
 * for managing upvotes, downvotes, comments, and retrieving the content of the post.
 * </p>
 *
 * @author Ropan Datta, L22
 * @version November 1, 2024
 */
public interface Post {

    /**
     * Retrieves the unique integer ID of the post.
     *
     * @return The ID of the post
     */
    int getPostId();

    /**
     * Retrieves the username of the user who created the post.
     *
     * @return The username of the creator
     */
    String getCreator();

    /**
     * Retrieves the number of users who upvoted the post.
     *
     * @return The number of upvotes for the post
     */
    int upvoteCounter();

    /**
     * Retrieves the number of users who downvoted the post.
     *
     * @return The number of downvotes for the post
     */
    int downvoteCounter();

    /**
     * Retrieves the IDs of the users who upvoted the post.
     *
     * @return A list of user IDs who upvoted the post
     */
    List<Integer> getUpvoteIds();

    /**
     * Retrieves the IDs of the users who downvoted the post.
     *
     * @return A list of user IDs who downvoted the post
     */
    List<Integer> getDownvoteIds();

    /**
     * Retrieves the comments made on the post.
     *
     * @return A list of comments associated with the post
     */
    List<Comment> getComments();

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
     * Retrieves the content of the post.
     *
     * @return The text content of the post
     */
    String getContent();

    /**
     * Adds an upvote to the post from a specified user.
     *
     * @param userId The ID of the user upvoting the post
     * @return true if the user's upvote was added successfully, false if the user had already
     * upvoted
     */
    boolean addUpvote(int userId);

    /**
     * Adds a downvote to the post from a specified user.
     *
     * @param userId The ID of the user downvoting the post
     * @return true if the user's downvote was added successfully, false if the user had already
     * downvoted
     */
    boolean addDownvote(int userId);

    /**
     * Adds a comment to the post.
     *
     * @param comment The comment to add to the post
     */
    void addComment(Comment comment);

    /**
     * Calculates and returns the score of the post, defined as the difference
     * between the number of upvotes and downvotes.
     *
     * @return The score of the post
     */
    default int calculateScore() {
        return upvoteCounter() - downvoteCounter();
    }
}
