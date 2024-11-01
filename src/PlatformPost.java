import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * PlatformPost
 *
 * This class represents a post on a platform that supports upvotes, downvotes, and comments.
 * Each post includes an owner ID, a body text, a header (optional), lists for upvotes and
 * downvotes, and a list of comments. This class implements the Post interface and is serializable.
 *
 * @author Rachel Rafik
 * @version November 1, 2024
 */
public class PlatformPost implements Post, Serializable {
    //FIELDS
    private Integer ownerId; // User ID of the post owner
    private String header; // Optional header for the post
    private String body; // Body text of the post
    private ArrayList<Integer> upvoteIds; // List of user IDs who upvoted the post
    private ArrayList<Integer> downvoteIds; // List of user IDs who downvoted the post
    private ArrayList<Comment> comments; // List of comments on the post

    //CONSTRUCTORS

    /**
     * Constructs a PlatformPost with the specified owner ID and body text.
     * Initializes upvote and downvote lists and sets the header to null.
     *
     * @param userId The ID of the user who owns the post
     * @param body The text content of the post
     */
    public PlatformPost(Integer userId, String body) {
        ownerId = userId;
        this.body = body;
        upvoteIds = new ArrayList<>();
        downvoteIds = new ArrayList<>();
        comments = new ArrayList<>();
        header = null;
    }

    //GETTERS/SETTERS

    /**
     * Retrieves the ID of the post owner.
     *
     * @return The user ID of the post owner
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * Retrieves the header of the post.
     *
     * @return The header text, or null if not set
     */
    public String getHeader() {
        return header;
    }

    /**
     * Retrieves the body of the post.
     *
     * @return The body text of the post
     */
    public String getBody() {
        return body;
    }

    /**
     * Adds a user ID to the list of upvotes.
     *
     * @param userId The ID of the user upvoting the post
     */
    public void addUpvote(int userId) {
        upvoteIds.add(userId);
    }

    /**
     * Adds a user ID to the list of downvotes.
     *
     * @param userId The ID of the user downvoting the post
     */
    public void addDownvote(int userId) {
        downvoteIds.add(userId);
    }

    /**
     * Retrieves the list of upvote IDs.
     *
     * @return A list of user IDs who upvoted the post
     */
    public ArrayList<Integer> getUpvoteIds() {
        return upvoteIds;
    }

    /**
     * Retrieves the list of downvote IDs.
     *
     * @return A list of user IDs who downvoted the post
     */
    public ArrayList<Integer> getDownvoteIds() {
        return downvoteIds;
    }

    /**
     * Retrieves the list of comments on the post.
     *
     * @return A list of comments associated with the post
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * Retrieves the title of the post. This method returns an empty string, as the title
     * is not currently used.
     *
     * @return An empty string
     */
    public String getTitle() {
        return "";
    }

    /**
     * Retrieves the total count of upvotes on the post.
     *
     * @return The number of upvotes
     */
    public int getUpvoteCount() {
        return upvoteIds.size();
    }

    /**
     * Retrieves the total count of downvotes on the post.
     *
     * @return The number of downvotes
     */
    public int getDownvoteCount() {
        return downvoteIds.size();
    }

    //METHODS

    /**
     * Adds a comment to the post.
     *
     * @param comment The comment to add to the post
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    /**
     * Counts and returns the total number of upvotes.
     *
     * @return The number of upvotes
     */
    public int upvoteCounter() {
        return upvoteIds.size();
    }

    /**
     * Counts and returns the total number of downvotes.
     *
     * @return The number of downvotes
     */
    public int downvoteCounter() {
        return downvoteIds.size();
    }
}
