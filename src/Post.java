import java.util.List;

/**
 * Post
 * 
 * The Post interface
 * 
 * @author Ropan Datta
 * @version
 * 
 */

public interface Post {
    /**
     * Retrieves the unique integer id of the post.
     * 
     * @return the integer id of the post
     */
    int getPostId();

    /**
     * Retrieves the id of the user who created the post.
     * 
     * @return the id of the user who created the post
     */
    int getCreatorId();

    /**
     * Retrieves the number of users who upvoted the post.
     * 
     * @return the number of users who upvoted the post
     */
    int upvoteCounter();

    /**
     * Retrieves the number of users who downvoted the post.
     * 
     * @return the number of users who downvoted the post
     */
    int downvoteCounter();

    /**
     * Retrieves the ids of the users who upvoted the post.
     * 
     * @return the list of ids of the users who upvoted the post
     */
    List<Integer> getUpvoteIds();

    /**
     * Retrieves the ids of the users who downvoted the post.
     * 
     * @return the list of ids of the users who downvoted the post
     */
    List<Integer> getDownvoteIds();

    /**
     * Retrieves the comments underneath the post.
     * 
     * @return the list of comments made underneath the post
     */
    List<Comment> getComments();

    /**
     * Retrieves the content of the post.
     * 
     * @return the content of the post
     */
    String getContent();

    /**
     * Attempts to add an upvote to the post.
     * 
     * @param userId the id of the user attempting to upvote the post
     * @return true if the user's upvote was added for the first time
     */
    boolean addUpvote(int userId);

    /**
     * Attempts to add a downvote to the post.
     * 
     * @param userId the id of the user attempting to downvote the post
     * @return true if the user's downvote was added for the first time
     */
    boolean addDownvote(int userId);

    void addComment(Comment comment);

    /**
     * Retrieves the 'score' of the post, i.e., the difference between the upvotes
     * and downvotes.
     * 
     * @return the difference between upvotes and downvotes
     */
    default int calculateScore() {
        return upvoteCounter() - downvoteCounter();
    }
}
