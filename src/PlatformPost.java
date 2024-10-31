import java.util.ArrayList;

public class PlatformPost {
    //FIELDS
    private Integer ownerId; //User ID who owns the post

    private String header; //Post header
    private String body; //Post message text

    private ArrayList<Integer> upvoteIds; //Number of upvotes
    private ArrayList<Integer> downvoteIds; //Number of downvotes
    private ArrayList<Comment> comments; //Comments under the post

    //CONSTRUCTORS
    public PlatformPost(Integer userId, String body) {
        ownerId = userId;
        this.body = body;
        upvoteIds = new ArrayList<>();
        downvoteIds = new ArrayList<>();
        header = null;
    }

    public PlatformPost(Integer userId, String header, String body) {
        ownerId = userId;
        this.header = header;
        this.body = body;
        upvoteIds = new ArrayList<>();
        downvoteIds = new ArrayList<>();
    }

    //GETTERS/SETTERS
    public Integer getOwnerId() {
        return ownerId;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public ArrayList<Integer> getUpvoteIds() {
        return upvoteIds;
    }

    public ArrayList<Integer> getDownvoteIds() {
        return downvoteIds;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    //METHODS

    /**
     *
     * @param comment
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    /**
     *
     * @param userId
     */
    public void addUpvote(Integer userId) {
        upvoteIds.add(userId);
    }

    /**
     *
     * @param userId
     */
    public void addDownvote(Integer userId) {
        downvoteIds.add(userId);
    }

    /**
     *
     * @return
     */
    public int upvoteCounter() {
        return upvoteIds.size();
    }

    /**
     *
     * @return
     */
    public int downvoteCounter() {
        return downvoteIds.size();
    }
}
