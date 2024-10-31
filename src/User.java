/**
 * User
 */

public interface User {
    int getId();

    int[] getFriendIds();

    int[] getBlockedUserIds();

    int[] getPostIds();

    int[] getCommentIds();
}
