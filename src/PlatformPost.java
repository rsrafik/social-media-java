import java.io.Serializable;
import java.util.*;
import java.awt.Image;

/**
 * PlatformPost
 * <p>
 * This class represents a post on a platform that supports upvotes, downvotes, and comments. Each
 * post includes an owner ID, post content, unique post ID, lists for upvotes and downvotes, and a
 * list of comments. This class implements the Post interface and is serializable.
 * </p>
 *
 * @author Rachel Rafik, L22
 * @version November 15, 2024
 */
public class PlatformPost implements Post, Serializable {
    private Integer id; // Unique ID for the post
    private Integer creatorId; // ID of the user who created the post
    private String content; // The text content of the post
    private Image image; // Image associated with the post
    private ArrayList<Integer> upvoteIds; // List of user IDs who upvoted the post
    private ArrayList<Integer> downvoteIds; // List of user IDs who downvoted the post
    private ArrayList<Comment> comments; // List of comments on the post

    /**
     * Constructs a PlatformPost with the specified creator ID and content. Initializes lists for
     * upvotes, downvotes, and comments.
     *
     * @param id the ID of the post
     * @param creatorId the ID of the user who created the post
     * @param content the text content of the post
     * @param image an optional image associated with the post
     */
    public PlatformPost(Integer id, Integer creatorId, String content, Image image) {
        this.id = id;
        this.creatorId = creatorId;
        this.content = content;
        this.image = image;

        this.upvoteIds = new ArrayList<>();
        this.downvoteIds = new ArrayList<>();
        this.comments = new ArrayList<>();
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
    public boolean hasImage() {
        return image != null;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public boolean addUpvote(int userId) {
        if (!upvoteIds.contains(userId)) {
            downvoteIds.remove((Integer) userId);
            upvoteIds.add(userId);
            return true;
        }
        return false;
    }

    @Override
    public boolean addDownvote(int userId) {
        if (!downvoteIds.contains(userId)) {
            upvoteIds.remove((Integer) userId);
            downvoteIds.add(userId);
            return true;
        }
        return false;
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
    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
