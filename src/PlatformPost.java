import java.io.Serializable;
import java.util.ArrayList;

/**
 * PlatformPost
 * <p>
 * This class represents a post on a platform that supports upvotes, downvotes, and comments.
 * Each post includes an owner ID, post content, unique post ID, lists for upvotes and
 * downvotes, and a list of comments. This class implements the Post interface and is serializable.
 * </p>
 *
 * @author Rachel Rafik, L22
 * @version November 1, 2024
 */
public class PlatformPost implements Post, Serializable {

    // FIELDS

    private static int postCount = 0;  // Tracks the total number of posts created

    private Integer postId; // Unique ID for the post
    private Integer creatorId; // ID of the user who created the post
    private String content; // The text content of the post
    private ArrayList<Integer> upvoteIds; // List of user IDs who upvoted the post
    private ArrayList<Integer> downvoteIds; // List of user IDs who downvoted the post
    private ArrayList<Comment> comments; // List of comments on the post

    // CONSTRUCTORS

    /**
     * Constructs a PlatformPost with the specified owner ID and content.
     * Initializes lists for upvotes, downvotes, and comments.
     *
     * @param creatorId The ID of the user who owns the post
     * @param content   The text content of the post
     */
    public PlatformPost(Integer creatorId, String content) {
        this.creatorId = creatorId;
        this.content = content;
        this.upvoteIds = new ArrayList<>();
        this.downvoteIds = new ArrayList<>();
        this.comments = new ArrayList<>();

        this.postId = postCount;
        postCount++;
    }

    // GETTERS/SETTERS

    /**
     * Retrieves the unique ID of the post.
     *
     * @return The post ID
     */
    public int getPostId() {
        return postId;
    }

    /**
     * Retrieves the ID of the post creator.
     *
     * @return The user ID of the post owner
     */
    public int getCreatorId() {
        return creatorId;
    }

    /**
     * Retrieves the content of the post.
     *
     * @return The body text of the post
     */
    public String getContent() {
        return content;
    }

    /**
     * Adds a user ID to the list of upvotes if not already present.
     *
     * @param userId The ID of the user upvoting the post
     * @return true if the user ID was successfully added, false if already present
     */
    public boolean addUpvote(int userId) {
        if (!upvoteIds.contains(userId)) {
            upvoteIds.add(userId);
            return true;
        }
        return false;
    }

    /**
     * Adds a user ID to the list of downvotes if not already present.
     *
     * @param userId The ID of the user downvoting the post
     * @return true if the user ID was successfully added, false if already present
     */
    public boolean addDownvote(int userId) {
        if (!downvoteIds.contains(userId)) {
            downvoteIds.add(userId);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the list of user IDs who upvoted the post.
     *
     * @return A list of user IDs who upvoted the post
     */
    public ArrayList<Integer> getUpvoteIds() {
        return upvoteIds;
    }

    /**
     * Retrieves the list of user IDs who downvoted the post.
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

    // METHODS

    /**
     * Adds a comment to the post.
     *
     * @param comment The comment to add to the post
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    /**
     * Counts and returns the total number of upvotes for the post.
     *
     * @return The number of upvotes
     */
    public int upvoteCounter() {
        return upvoteIds.size();
    }

    /**
     * Counts and returns the total number of downvotes for the post.
     *
     * @return The number of downvotes
     */
    public int downvoteCounter() {
        return downvoteIds.size();
    }
}
