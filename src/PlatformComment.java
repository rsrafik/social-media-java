import java.io.Serializable;
import java.util.ArrayList;

/**
 * PlatformComment
 *
 * This class represents a comment on a platform that supports upvotes and downvotes.
 * Each comment includes a creator ID, a message, and lists to track user IDs for upvotes
 * and downvotes. This class implements the Comment interface and is serializable.
 *
 * @author Rachel Rafik
 * @version November 1, 2024
 */
public class PlatformComment implements Comment, Serializable {
    private Integer creatorID; // ID of the user who created the comment
    private String message; // The content of the comment

    private ArrayList<Integer> upvoteIds; // List of user IDs who upvoted the comment
    private ArrayList<Integer> downvoteIds; // List of user IDs who downvoted the comment

    /**
     * Retrieves the ID of the user who created the comment.
     *
     * @return The creator's user ID
     */
    public int getCreatorId() {
        return creatorID;
    }

    /**
     * Counts and returns the number of upvotes for the comment.
     *
     * @return The total number of upvotes
     */
    public int upvoteCounter() {
        return upvoteIds.size();
    }

    /**
     * Counts and returns the number of downvotes for the comment.
     *
     * @return The total number of downvotes
     */
    public int downvoteCounter() {
        return downvoteIds.size();
    }

    /**
     * Adds a user ID to the list of upvotes for the comment.
     *
     * @param userId The ID of the user who upvoted the comment
     */
    public void addUpvote(int userId) {
        upvoteIds.add(userId);
    }

    /**
     * Adds a user ID to the list of downvotes for the comment.
     *
     * @param userId The ID of the user who downvoted the comment
     */
    public void addDownvote(int userId) {
        downvoteIds.add(userId);
    }
}
