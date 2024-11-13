import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * PlatformPost
 * <p>
 * This class represents a post on a platform that supports upvotes, downvotes, and comments. Each
 * post includes an owner ID, post content, unique post ID, lists for upvotes and downvotes, and a
 * list of comments. This class implements the Post interface and is serializable.
 * </p>
 *
 * @author Rachel Rafik, L22
 * @version November 1, 2024
 */
public class PlatformPost implements Post, Serializable {

    // Static Fields
    // private static final String POST_COUNT_FILE = "postCount.dat";
    private static int postCount = 0;
    // private static int postCount = loadPostCount(); // Load postCount from file on startup

    // Instance Fields
    private Integer postId; // Unique ID for the post
    private int creatorId; // username of the user who created the post
    private String content; // The text content of the post
    private ArrayList<Integer> upvoteIds; // List of user IDs who upvoted the post
    private ArrayList<Integer> downvoteIds; // List of user IDs who downvoted the post
    private ArrayList<Comment> comments; // List of comments on the post
    private BufferedImage image; // Image associated with the post

    // Constructors

    /**
     * Constructs a PlatformPost with the specified creator ID and content. Initializes lists for
     * upvotes, downvotes, and comments.
     *
     * @param creator The username of the user who created the post
     * @param content The text content of the post
     */
    public PlatformPost(int creatorId, String content, BufferedImage image) {
        this.postId = postCount++;

        this.creatorId = creatorId;
        this.content = content;
        this.image = image;

        this.upvoteIds = new ArrayList<>();
        this.downvoteIds = new ArrayList<>();
        this.comments = new ArrayList<>();
        // savePostCount(); // Save the incremented postCount to the file
    }

    // /**
    // * Constructs a PlatformPost with the specified creator ID, content, and image.
    // * Initializes lists for upvotes, downvotes, and comments.
    // *
    // * @param creator The ID of the user who created the post
    // * @param content The text content of the post
    // * @param image The image associated with the post
    // */
    // public PlatformPost(String creator, String content, BufferedImage image) {
    // this.creator = creator;
    // this.content = content;
    // this.upvoteIds = new ArrayList<>();
    // this.downvoteIds = new ArrayList<>();
    // this.comments = new ArrayList<>();
    // this.image = image;

    // this.postId = postCount++;
    // savePostCount(); // Save the incremented postCount to the file
    // }

    // Static Method to Load postCount from a File
    // private static int loadPostCount() {
    // try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(POST_COUNT_FILE))) {
    // return in.readInt();
    // } catch (IOException e) {
    // // File does not exist or cannot be read, default to 0
    // return 0;
    // }
    // }

    // // Static Method to Save postCount to a File
    // private static void savePostCount() {
    // try (ObjectOutputStream out =
    // new ObjectOutputStream(new FileOutputStream(POST_COUNT_FILE))) {
    // out.writeInt(postCount);
    // } catch (IOException e) {
    // System.err.println("Error saving post count: " + e.getMessage());
    // }
    // }

    // Getters/Setters and Methods

    /**
     * Retrieves the unique ID of the post.
     *
     * @return The post ID
     */
    @Override
    public int getPostId() {
        return postId;
    }

    /**
     * Retrieves the username of the post creator.
     *
     * @return The username of the post owner
     */
    @Override
    public int getCreatorId() {
        return creatorId;
    }

    /**
     * Retrieves the content of the post.
     *
     * @return The body text of the post
     */
    @Override
    public String getContent() {
        return content;
    }

    /**
     * Adds a user ID to the list of upvotes if not already present.
     *
     * @param userId The ID of the user upvoting the post
     * @return true if the user ID was successfully added, false if already present
     */
    @Override
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
    @Override
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
    @Override
    public ArrayList<Integer> getUpvoteIds() {
        return upvoteIds;
    }

    /**
     * Retrieves the list of user IDs who downvoted the post.
     *
     * @return A list of user IDs who downvoted the post
     */
    @Override
    public ArrayList<Integer> getDownvoteIds() {
        return downvoteIds;
    }

    /**
     * Retrieves the list of comments on the post.
     *
     * @return A list of comments associated with the post
     */
    @Override
    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * Retrieves the image associated with the post.
     *
     * @return Image of the post
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Checks if the post has an image.
     *
     * @return true if the image exists, otherwise false
     */
    public boolean hasImage() {
        return image != null;
    }

    /**
     * Adds a comment to the post.
     *
     * @param comment The comment to add to the post
     */
    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    /**
     * Counts and returns the total number of upvotes for the post.
     *
     * @return The number of upvotes
     */
    @Override
    public int upvoteCounter() {
        return upvoteIds.size();
    }

    /**
     * Counts and returns the total number of downvotes for the post.
     *
     * @return The number of downvotes
     */
    @Override
    public int downvoteCounter() {
        return downvoteIds.size();
    }
}
