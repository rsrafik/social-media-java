/**
 * OperationType - Defines the types of operations that can be sent to the server.
 * This enum is used to identify the client's requests to the server.
 *
 * @version November 17, 2024
 */
public enum OperationType {
    IS_LOGGEDIN,
    LOGIN,
    LOGOUT,
    CREATE_USER,
    CREATE_POST,
    SEND_FRIENDREQUEST,
    TESTING,
    CHECK_USERNAME,        // New operation to check username availability
    GET_ALL_POSTS,        // New operation to retrieve all posts
    GET_USER_POSTS,       // New operation to retrieve posts of a specific user
    GET_FRIENDS_LIST,     // New operation to retrieve the user's friends list
    GET_FRIEND_REQUESTS,   // New operation to retrieve the user's friend requests
    GET_USER
}
