# README

## Compiling and Running

**[DO THIS FIRST] Server:**

```bash

-> COMPILING

javac -cp src -d build/server src/PlatformServer.java

-> RUNNING

java -cp build/server PlatformServer

```

**Client:**

```bash
-> Compiling

javac -cp src:lib/\* -d build/client src/PlatformRunner.java

-> Running

java -cp build/client:lib/\* PlatformRunner

```

## Submission Details

**Rachel Rafik** - Submitted the Project Report

**Rachel Rafik** - Submitted the Presentation

**Ropan Datta** - Submitted the project to the Phase 3 Workspace

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
    - First, we check that Database is a real interface, and then the class PlatformDatabase implements Database.
    - Second, we test the ability to read user and post files. We created a test file for each and then read them back and compared that to the expected. It also checks that the program reacts correctly to an empty file
    - Third, we test adding users and posts to the database. We created an empty database object and used the add method for user and post separately, and then read the lists of users and posts to make sure they were the correct size.
    - Fourth, we tested whether the getters for both the Users and Posts array lists within the database were working correctly by creating an empty database object and creating and adding two new user and post objects to the respective array lists. Then we called the respective methods to then make sure that they consisted of the previously added objects within the respective array lists.
    - we also made similar tests for the functions of blocking, following, commenting, upvoting/ down voting, and search user
        
        
    - **updated the testcase with new parameters in each example user and post to work with new constructors. Also the class was changed to PlatformDatabase instead of FoundationDatabase
    
- Relationship
    - Template for PlatformDatabase, implements Database

### Client Handler

- Functionality
    - Methods to update and query the database.
- Relationship
    - Interface for PlatformClientHandler
- Testing
    - Used JUNIT to check that the ClientHandler interface exists and also is an interface and not a class. Then we check that ClientHandler is implemented by using reflection to check if PlatformClientHandler either directly implements ClientHandler or is a subclass of a class that implements ClientHandler.

## Classes

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

### PlatformDatabase

- Functionality
    - This class manages a database of User objects and their associated Posts. It provides* methods to read users from a file, add new users, retrieve all users, and retrieve all posts.* Users are stored in a serialized file format and loaded upon instantiation.
- Relationship
    - Implements Database to inherit all the functionality of User as well as be serializable for input and output object stream.

### Platform Client

- Functionality
    - Communicates with the server on port 5000.
- Relationship
    - Writes operation types and operation data to the server and reads in the success status of the operation.
- Testing
    - Our client only has a main method that connects it to the server and collects inputs, so we couldn’t make a junit testcase for it. Instead, I just ran it and made sure that it successfully connected to the server. I would use the different operations available and made sure that server was able to get the inputs and make changes to the files accordingly

### Platform Client Handler

- Functionality
    - Handles a single client in a new Thread
- Relationship
    - Implements ClientHandler to be Runnable and update the database.
- Testing
    - There is a test for all methods: run, loggedIn, logIn, logOut, createUser, createPost, as well as Friend Request.
    - The run method is tested through the PlatformClientHandlerRunTestCase class where a mock socket is created to simulate client and server communication.
    - Methods loggedIn, logIn, logOut, createUser, and createPost are tested through the PlatformClientHandlerTestCase class where mock sockets are created following different simulations such as logging in, logging out, creating a user, creating a post, making sure users can’t log in with incorrect credentials and so on to test that each method works as intended.
    - The client handler is where friend requests are processed, so I created a test for different possible scenarios when sending a friend request. The test case makes sure the correct result is returned when a friend request is sent to a nonexistent user, a blocked user, a user that is already a friend. It also tests sending a friend request to a real user.

### User Info

- Functionality
    - Provides public information about a User
- Relationship
    - Stores user information except the sensitive information, such as passwords and followers.
- Testing
- UserInfo has the same testing as the User and PlatformUser class due to UserInfo building off of User and consisting off similar methods.
