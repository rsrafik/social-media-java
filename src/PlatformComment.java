import java.io.Serializable;
import java.util.*;

/**
 * PlatformComment
 * <p>
 * This class represents a comment on a platform that supports upvotes and downvotes. Each comment
 * includes a creator ID, a content, and lists to track user IDs for upvotes and downvotes. This
 * class implements the Comment interface and is serializable.
 * </p>
 * 
 * @author Rachel Rafik, L22
 * @version November 15, 2024
 */
public class PlatformComment implements Comment, Serializable {
    private final Integer id;
    private final Integer creatorId; // ID of the user who created the comment
    private final String content; // The content of the comment

    private final ArrayList<Integer> upvoteIds; // List of user IDs who upvoted the comment
    private final ArrayList<Integer> downvoteIds; // List of user IDs who downvoted the comment

    /**
     * Constructs a PlatformComment with a specified creator ID and content. Initializes empty lists
     * for upvotes and downvotes.
     *
     * @param creatorId the ID of the user who created the comment
     * @param content the text content of the comment
     */
    public PlatformComment(Integer id, Integer creatorId, String content) {
        this.id = id;
        this.creatorId = creatorId;
        this.content = content;

        upvoteIds = new ArrayList<>();
        downvoteIds = new ArrayList<>();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Integer getCreatorId() {
        return creatorId;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public List<Integer> getUpvoteIds() {
        return upvoteIds;
    }

    @Override
    public List<Integer> getDownvoteIds() {
        return downvoteIds;
    }

    @Override
    public int upvoteCounter() {
        return upvoteIds.size();
    }

    @Override
    public int downvoteCounter() {
        return downvoteIds.size();
    }

    @Override
    public void addUpvote(int userId) {
        if (!upvoteIds.contains(userId)) {
            downvoteIds.remove((Integer) userId); // remove earlier downvote if it exists
            upvoteIds.add(userId);
        }
    }

    @Override
    public void addDownvote(int userId) {
        if (!downvoteIds.contains(userId)) {
            upvoteIds.remove((Integer) userId); // remove earlier upvote if it exists
            downvoteIds.add(userId);
        }
    }

    @Override
    public void removeUpvote(int userId) {
        upvoteIds.remove((Integer) userId);
    }

    @Override
    public void removeDownvote(int userId) {
        downvoteIds.remove((Integer) userId);
    }
}
