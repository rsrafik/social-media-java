/**
 * Comment
 * 
 * @author Ropan Datta
 * @version
 * 
 */

public interface Comment {
    int getOwnerId();

    int getPostId();

    int getUpvotes();

    int getDownvotes();

    void addUpvote(int userId);

    void addDownvote(int userId);

    default int getScore() {
        return getUpvotes() - getDownvotes();
    }
}
