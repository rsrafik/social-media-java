import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * SampleMenu - A menu-driven system for managing user authentication,
 * profile viewing, and post creation on a simple social media platform.
 *
 * This version communicates with a server for all data operations.
 *
 * @version November 17, 2024
 */
public class SampleMenu {
    // Divider used for output separation
    private static final String DIVIDER = "_______________________";

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5001);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            PlatformUser currentUser = processUserAuthentication(scanner, outputStream, inputStream);
            if (currentUser == null) {
                System.out.println("Exiting program due to failed authentication.");
                return;
            }
            System.out.println(DIVIDER);

            processHomePage(scanner, currentUser, outputStream, inputStream);
            System.out.println(DIVIDER);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static PlatformUser processUserAuthentication(Scanner scanner, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        int welcomeChoice = openingMessage(scanner);
        PlatformUser currentUser = null;
        boolean done = false;

        while (!done) {
            switch (welcomeChoice) {
                case 1:
                    currentUser = handleLogin(scanner, outputStream, inputStream);
                    if (currentUser != null) {
                        done = true;
                    } else {
                        welcomeChoice = promptRetryOrSignup(scanner);
                    }
                    break;
                case 2:
                    currentUser = handleSignup(scanner, outputStream, inputStream);
                    done = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        return currentUser;
    }

    private static int openingMessage(Scanner scanner) {
        System.out.println("Welcome to Twitter 2.0!");
        System.out.println("Select an option");
        System.out.println("[1] Log In");
        System.out.println("[2] Sign Up");

        String choice = scanner.nextLine();
        while (!choice.equals("1") && !choice.equals("2")) {
            System.out.println("Error! Enter valid input.");
            choice = scanner.nextLine();
        }
        return Integer.parseInt(choice);
    }

    private static PlatformUser handleLogin(Scanner scanner, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        outputStream.writeObject(OperationType.LOGIN);
        outputStream.writeObject(username);
        outputStream.writeObject(password);
        outputStream.flush();

        boolean success = (boolean) inputStream.readObject();
        if (success) {
            // Retrieve existing PlatformUser instead of creating a new one
            outputStream.writeObject(OperationType.GET_USER);
            outputStream.writeObject(username);
            outputStream.flush();

            PlatformUser existingUser = (PlatformUser) inputStream.readObject(); // Assumes the server sends back the user object
            if (existingUser != null) {
                System.out.println("Login successful!");
                return existingUser;
            } else {
                System.out.println("Error fetching user data.");
                return null;
            }
        } else {
            System.out.println("Incorrect username or password.");
            return null;
        }
    }

    private static int promptRetryOrSignup(Scanner scanner) {
        while (true) {
            System.out.println("[1] Try Again");
            System.out.println("[2] Sign Up");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                return 1;
            } else if (choice.equals("2")) {
                return 2;
            }
            System.out.println("Error! Enter valid input.");
        }
    }

    private static PlatformUser handleSignup(Scanner scanner, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        System.out.println("Sign Up");
        System.out.println("Enter a new username:");
        String username = scanner.nextLine();

        outputStream.writeObject(OperationType.CHECK_USERNAME);
        outputStream.writeObject(username);
        outputStream.flush();
        boolean usernameAvailable = (boolean) inputStream.readObject();
        while (!usernameAvailable) {
            System.out.println("Username already exists. Please enter a different username:");
            username = scanner.nextLine();
            outputStream.writeObject(OperationType.CHECK_USERNAME);
            outputStream.writeObject(username);
            outputStream.flush();
            usernameAvailable = (boolean) inputStream.readObject();
        }

        String password;
        while (true) {
            System.out.println("Enter a new password (minimum 8 characters):");
            password = scanner.nextLine();
            if (password.length() >= 8) {
                break;
            }
            System.out.println("Password too short. Please enter at least 8 characters.");
        }

        outputStream.writeObject(OperationType.CREATE_USER);
        outputStream.writeObject(username);
        outputStream.writeObject(password);
        outputStream.flush();
        boolean success = (boolean) inputStream.readObject();
        System.out.println(success);

        if (success) {
            // Retrieve the newly created PlatformUser
            PlatformUser newUser = (PlatformUser) inputStream.readObject();
            System.out.println(newUser.getUsername());
            if (newUser != null) {
                System.out.println("Sign-up successful! You can now log in with your new credentials.");
                return newUser;
            } else {
                System.out.println("Sign-up succeeded but failed to retrieve user data.");
                return null;
            }
        } else {
            System.out.println("Sign-up failed. Please try again.");
            return null;
        }
    }

    private static void processHomePage(Scanner scanner, PlatformUser currentUser, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        boolean done = false;
        while (!done) {
            int homepageChoice = displayHomePage(scanner);

            switch (homepageChoice) {
                case 1:
                    loadPostsPage(outputStream, inputStream);
                    break;
                case 2:
                    loadUserData(currentUser, scanner, outputStream, inputStream);
                    break;
                case 3:
                    createNewPost(scanner, currentUser, outputStream, inputStream);
                    break;
                case 4:
                    logout(outputStream);
                    done = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static int displayHomePage(Scanner scanner) {
        System.out.println("Choose an option:");
        System.out.println("[1] View Homepage");
        System.out.println("[2] View your profile");
        System.out.println("[3] Create new post");
        System.out.println("[4] Log out and Exit");

        String choice = scanner.nextLine();
        while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")) {
            System.out.println("Error! Enter valid input.");
            choice = scanner.nextLine();
        }
        return Integer.parseInt(choice);
    }

    private static void loadPostsPage(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        outputStream.writeObject(OperationType.GET_ALL_POSTS);
        outputStream.flush();
        LinkedHashMap<Integer, PlatformPost> posts = (LinkedHashMap<Integer, PlatformPost>) inputStream.readObject();

        if (posts.isEmpty()) {
            System.out.println("No posts available.");
        } else {
            System.out.println("Displaying all posts:");
            for (Map.Entry<Integer, PlatformPost> entry : posts.entrySet()) {
                PlatformPost post = entry.getValue();
                System.out.println("Creator ID: " + post.getCreatorId());
                System.out.println("Content: " + post.getContent());
                if (post.hasImage()) {
                    System.out.println("[Image Attached]");
                }
                System.out.println("Upvotes: " + post.upvoteCounter());
                System.out.println("Downvotes: " + post.downvoteCounter());
                System.out.println(DIVIDER);
            }
        }
    }

    private static void loadUserData(PlatformUser currentUser, Scanner scanner, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        System.out.println("Username: " + currentUser.getUsername());
        boolean valid = false;

        while (!valid) {
            System.out.println("Choose an option:");
            System.out.println("[1] View your posts");
            System.out.println("[2] View friend's list");
            System.out.println("[3] View friend requests");
            System.out.println("[4] Add friend");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewUserPosts(currentUser, outputStream, inputStream);
                    valid = true;
                    break;
                case "2":
                    viewFriendsList(currentUser, outputStream, inputStream);
                    valid = true;
                    break;
                case "3":
                    viewFriendRequests(currentUser, outputStream, inputStream);
                    valid = true;
                    break;
                case "4":
                    addFriend(scanner, currentUser, outputStream, inputStream);
                    valid = true;
                    break;
                default:
                    System.out.println("Error! Enter valid input.");
            }
        }
    }

    private static void viewUserPosts(PlatformUser currentUser, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        outputStream.writeObject(OperationType.GET_USER_POSTS);
        outputStream.writeObject(currentUser.getUsername());
        outputStream.flush();

        List<PlatformPost> userPosts = (List<PlatformPost>) inputStream.readObject();
        if (userPosts.isEmpty()) {
            System.out.println("You have no posts.");
        } else {
            for (PlatformPost post : userPosts) {
                System.out.println("Post ID: " + post.getId());
                System.out.println("Content: " + post.getContent());
                if (post.hasImage()) {
                    System.out.println("[Image Attached]");
                }
                System.out.println("Upvotes: " + post.upvoteCounter());
                System.out.println("Downvotes: " + post.downvoteCounter());
                System.out.println(DIVIDER);
            }
        }
    }

    private static void viewFriendsList(PlatformUser currentUser, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        outputStream.writeObject(OperationType.GET_FRIENDS_LIST);
        outputStream.writeObject(currentUser.getUsername());
        outputStream.flush();

        List<String> friends = (List<String>) inputStream.readObject();
        if (friends.isEmpty()) {
            System.out.println("You have no friends.");
        } else {
            for (String friend : friends) {
                System.out.println(friend);
                System.out.println(DIVIDER);
            }
        }
    }

    private static void viewFriendRequests(PlatformUser currentUser, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        outputStream.writeObject(OperationType.GET_FRIEND_REQUESTS);
        outputStream.writeObject(currentUser.getUsername());
        outputStream.flush();

        List<String> friendRequests = (List<String>) inputStream.readObject();
        if (friendRequests.isEmpty()) {
            System.out.println("No friend requests.");
        } else {
            for (String request : friendRequests) {
                System.out.println("Friend request from: " + request);
                System.out.println(DIVIDER);
            }
        }
    }

    private static void addFriend(Scanner scanner, PlatformUser currentUser, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        System.out.println("Enter their username:");
        String username = scanner.nextLine();
        System.out.println("Enter a message:");
        String message = scanner.nextLine();

        outputStream.writeObject(OperationType.SEND_FRIENDREQUEST);
        outputStream.writeObject(username);
        outputStream.writeObject(currentUser.getUsername());
        outputStream.writeObject(message);
        outputStream.flush();

        boolean success = (boolean) inputStream.readObject();
        if (success) {
            System.out.println("Friend request sent.");
        } else {
            System.out.println("Failed to send friend request.");
        }
    }

    private static void createNewPost(Scanner scanner, PlatformUser currentUser, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        while (true) {
            System.out.println("Enter your message:");
            String message = scanner.nextLine();
            System.out.println("Your message is:");
            System.out.println(message);
            System.out.println("Upload Post?");
            System.out.println("[1] Upload");
            System.out.println("[2] Rewrite");
            System.out.println("[3] Cancel");

            String choice = scanner.nextLine();
            while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
                System.out.println("Error! Enter valid input.");
                choice = scanner.nextLine();
            }
            int choiceInt = Integer.parseInt(choice);

            switch (choiceInt) {
                case 1:
                    Integer userId = currentUser.getId();
                    if (userId == null) {
                        System.out.println("Error: User ID is null. Cannot create post.");
                        return;
                    }

                    System.out.println("Sending CREATE_POST request...");
                    outputStream.writeObject(OperationType.CREATE_POST);
                    outputStream.writeObject(currentUser.getId());
                    outputStream.writeObject(message);
                    outputStream.writeObject(null);
                    outputStream.flush();

                    boolean success = (boolean) inputStream.readObject();
                    System.out.println("made it this far!!");
                    System.out.println("Post upload response: " + success);
                    if (success) {
                        System.out.println("Post uploaded successfully!");
                    } else {
                        System.out.println("Failed to upload post.");
                    }
                    return;
                case 2:
                    continue;
                case 3:
                    System.out.println("Operation cancelled.");
                    return;
            }
        }
    }

    private static void logout(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeObject(OperationType.LOGOUT);
        outputStream.flush();
        System.out.println("Logging you out...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thank you for using Twitter 2.0");
    }
}
