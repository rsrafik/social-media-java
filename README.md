# README

## How to compile

javac Testing.java

java Testing

## Submission Details

**Rachel Rafik** - Vocareum Phase 1 Workspace

# Description

## Interfaces

### User

- Functionality
    - Defines the structure for a user on a platform, including methods  for managing the user's friends, blocked users, posts, and friend requests. It provides methods for retrieving user information, updating usernames and passwords, and verifying login credentials.
- Testing
    - Used JUNIT to check that the User interface exists and also is an interface and not a class. Then we check that User is implemented by creating a PlatformUser object using JUNIT test to check that it is an instance of User
- Relationship
    - Template for PlatformUser, PlatformUser implements User

### Comment

- Functionality
    - Defines the structure for a comment in a platform, including methods for retrieving the creator ID, managing upvotes and downvotes, and calculating the comment's score.
- Testing
    - Used JUNIT to check that the Comment interface exists and also is an interface and not a class. Then we check that Comment is implemented by using reflection to check if Platform comment either directly implements comment or is a subclass of a class that implements comment.
- Relationship
    - Template for PlatformComment, PlatformComment implements Comment

### FriendRequest

- Functionality
    - Defines the structure for a friend request on a platform, including methods for retrieving the user ID of the sender and the message associated with the request.
- Testing
    - Used JUNIT to check that the FriendRequest interface exists and also is an interface and not a class. Then we check that FriendRequest is implemented by creating a PlatformFriendRequest object using JUNIT test to check that it is an instance of FriendRequest
- Relationship
    - Template for PlatformFriendRequest, PlatformRequest implements FriendRequest

### Post

- Functionality
    - This interface defines the structure for a post on a platform, including methods for managing upvotes, downvotes, comments, and retrieving the content of the post.
- Testing
    - Used JUNIT to check that the Post interface exists and also is an interface and not a class. Then we check that Post is implemented by using reflection to check if PlatformPost either directly implements Post or is a subclass of a class that implements Post.
- Relationship
    - Template for PlatformPost, PlatformPost implements Post

### Database

- Functionality
    - Refining methods to manage users and posts in a persistent storage system.
- Testing
    - First, we check that Database is a real interface, and then the class FoundationDatabase implements Database.
    - Second, we test the ability to read user and post files. We created a test file for each and then read them back and compared that to the expected. It also checks that the program reacts correctly to an empty file
    - Third, we test adding users and posts to the database. We created an empty database object and used the add method for user and post separately, and then read the lists of users and posts to make sure they were the correct size.
    - Fourth, we tested whether the getters for both the Users and Posts array lists within the database were working correctly by creating an empty database object and creating and adding two new user and post objects to the respective array lists. Then we called the respective methods to then make sure that they consisted of the previously added objects within the respective array lists.
- Relationship
    - Template for FoundationDatabase, FoundationDatabase implements Database

## Classes

---

### Testing

- Functionality
    - This class tests the functionality of the FoundationDatabase by adding 100 sample users, saving them to a file, and then reading the file back to display the users.* If the file "users.dat" already exists, it is deleted at the start to ensure a fresh test.
- Relationship
    - Tests if the methods of DatabaseFoundation actually work and create users, and test if they run in order, synchronized.

### PlatformUser

- Functionality
    - Represents a user on the platform with unique user ID, username, password, and lists to store posts, friends, blocked users, and friend requests. It allows management of friends and blocked users, and implements the User interface. This class is serializable to allow persistence.
- Relationship
    - Implements User, Serializable to inherit all the functionality of User as well as be serializable for input and output object stream.

### PlatformPost

- Functionality
    - This class represents a post on a platform that supports upvotes, downvotes, and comments. Each post includes an owner ID, post content, unique post ID, lists for upvotes and downvotes, and a list of comments. This class implements the Post interface and is serializable.
- Relationship
    - Implements Post, Serializable to inherit all the functionality of Post as well as be serializable for input and output object stream.

### PlatformComment

- Functionality
    - This class represents a comment on a platform that supports upvotes and downvotes. Each comment includes a creator ID, a message, and lists to track user IDs for upvotes and downvotes. This class implements the Comment interface and is serializable.
- Relationship
    - Implements Comment, Serializable to inherit all the functionality of Comment as well as be serializable for input and output object stream.

### PlatformFriendRequest

- Functionality
    - This class represents a friend request on the platform, containing the ID of the user who sent the request and an optional message. The class implements the FriendRequest interface and is serializable to allow for persistence.
- Relationship
    - Implements FriendRequest, Serializable to inherit all the functionality of FriendRequest as well as be serializable for input and output object stream.

### FoundationDatabase

- Functionality
    - This class manages a database of User objects and their associated Posts. It provides* methods to read users from a file, add new users, retrieve all users, and retrieve all posts.* Users are stored in a serialized file format and loaded upon instantiation.
- Relationship
    - Implements Database to inherit all the functionality of User as well as be serializable for input and output object stream.
