/**
 * OperationType
 * <p>
 * This enum provides a list of operations the server provides to the client.
 * </p>
 * 
 * @author Ropan Datta, L22
 * @version December 2, 2022
 */

public enum OperationType {
    IS_LOGGEDIN, LOGIN, LOGOUT, CREATE_USER, CREATE_POST, GET_BLOCKED_USERS, BLOCK_USER, UNBLOCK_USER, GET_FRIENDREQUESTS, SEND_FRIENDREQUEST, CANCEL_FRIENDREQUEST, FETCH_POST, UPVOTE_POST, DOWNVOTE_POST, FETCH_COMMENTS, SEARCH_USER, LOAD_FEED, TESTING
}
