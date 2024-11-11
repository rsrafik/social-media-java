import java.io.File;
import java.util.*;

/**
 * SampleMenu - A menu-driven system for managing user authentication,
 * profile viewing, and post creation on a simple social media platform.
 *
 * This program simulates a basic command-line interface allowing
 * users to sign in, sign up, view posts, and add new posts using
 * a menu system.
 *
 * @author Rachel Rafik
 * @version November 11, 2024
 */
public class SampleMenu {
    // Divider used for output separation
    private static final String DIVIDER = "_______________________";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File userData = new File("users.dat");
        File postData = new File("posts.dat");
        File postCountData = new File("postCount.dat");
        File userCountData = new File("userCount.dat");

        initializeFreshData(scanner, userData, postData, postCountData, userCountData);

        FoundationDatabase fd = new FoundationDatabase();
        fd.readUsers();
        fd.readPosts();
        LinkedHashMap<String, PlatformUser> users = fd.getAllUsers();

        PlatformUser currentUser = processUserAuthentication(scanner, users, fd);
        System.out.println(DIVIDER);

        processHomePage(scanner, currentUser, fd);
        System.out.println(DIVIDER);
    }

    /**
     * Initializes fresh data files if prompted by the user.
     *
     * @param scanner       Scanner object for user input
     * @param userData      File object for user data
     * @param postData      File object for post data
     * @param postCountData File object for post count data
     * @param userCountData File object for user count data
     */
    private static void initializeFreshData(Scanner scanner, File userData, File postData, File postCountData, File userCountData) {
        System.out.println("<TESTING ONLY> Start Fresh?");
        String delete = scanner.nextLine();

        if (delete.equals("y")) {
            deleteFileIfExists(userData);
            deleteFileIfExists(postData);
            deleteFileIfExists(postCountData);
            deleteFileIfExists(userCountData);
        }
    }

    /**
     * Deletes a file if it exists.
     *
     * @param file File to be deleted
     */
    private static void deleteFileIfExists(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Handles user authentication through login or signup.
     *
     * @param scanner Scanner object for user input
     * @param users   LinkedHashMap containing PlatformUser data
     * @param fd      FoundationDatabase object for data management
     * @return The authenticated PlatformUser object
     */
    private static PlatformUser processUserAuthentication(Scanner scanner, LinkedHashMap<String, PlatformUser> users, FoundationDatabase fd) {
        int welcomeChoice = openingMessage(scanner);
        PlatformUser currentUser = null;
        boolean done = false;

        while (!done) {
            switch (welcomeChoice) {
                case 1:
                    currentUser = handleLogin(scanner, users);
                    if (currentUser != null) {
                        done = true;
                    } else {
                        welcomeChoice = promptRetryOrSignup(scanner);
                    }
                    break;
                case 2:
                    currentUser = handleSignup(scanner, users, fd);
                    done = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        return currentUser;
    }

    /**
     * Displays the opening message and prompts the user to select
     * between logging in or signing up.
     *
     * @param scanner The Scanner object for user input
     * @return An integer representing the user's choice
     */
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

    /**
     * Handles user login by prompting for username and password.
     *
     * @param scanner Scanner object for user input
     * @param users   LinkedHashMap containing PlatformUser data
     * @return The authenticated PlatformUser object, or null if authentication fails
     */
    private static PlatformUser handleLogin(Scanner scanner, LinkedHashMap<String, PlatformUser> users) {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        PlatformUser user = users.get(username);
        if (user == null) {
            System.out.println("Username not found.");
            return null;
        }

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        if (user.testPassword(password)) {
            System.out.println("Login successful!");
            return user;
        } else {
            System.out.println("Incorrect password.");
            return null;
        }
    }

    /**
     * Prompts the user to retry logging in or to sign up.
     *
     * @param scanner Scanner object for user input
     * @return An integer representing the user's next choice
     */
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

    /**
     * Handles user signup by creating a new user and adding them to the database.
     *
     * @param scanner Scanner object for user input
     * @param users   LinkedHashMap containing PlatformUser data
     * @param fd      FoundationDatabase object for data management
     * @return The newly created PlatformUser object
     */
    private static PlatformUser handleSignup(Scanner scanner, LinkedHashMap<String, PlatformUser> users, FoundationDatabase fd) {
        System.out.println("Sign Up");
        System.out.println("Enter a new username:");
        String username = scanner.nextLine();

        while (users.containsKey(username)) {
            System.out.println("Username already exists. Please enter a different username:");
            username = scanner.nextLine();
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

        PlatformUser newUser = new PlatformUser(username, password);
        users.put(username, newUser);
        fd.addUser(newUser);
        System.out.println("Sign-up successful! You can now log in with your new credentials.");
        return newUser;
    }

    /**
     * Handles the home page menu after user authentication.
     *
     * @param scanner     Scanner object for user input
     * @param currentUser The authenticated PlatformUser object
     * @param fd          FoundationDatabase object for data management
     */
    private static void processHomePage(Scanner scanner, PlatformUser currentUser, FoundationDatabase fd) {
        boolean done = false;
        while (!done) {
            int homepageChoice = displayHomePage(scanner);

            switch (homepageChoice) {
                case 1:
                    loadPostsPage(fd);
                    break;
                case 2:
                    loadUserData(currentUser, scanner, fd);
                    break;
                case 3:
                    createNewPost(scanner, currentUser, fd);
                    break;
                case 4:
                    logout();
                    done = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the home page menu and prompts the user for a choice.
     *
     * @param scanner The Scanner object for user input
     * @return An integer representing the user's choice
     */
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

    /**
     * Loads and displays all posts from the database.
     *
     * @param fd FoundationDatabase object for data management
     */
    private static void loadPostsPage(FoundationDatabase fd) {
        LinkedHashMap<Integer, PlatformPost> posts = fd.getAllPosts();

        if (posts.isEmpty()) {
            System.out.println("No posts available.");
        } else {
            System.out.println("Displaying all posts:");
            for (Map.Entry<Integer, PlatformPost> entry : posts.entrySet()) {
                PlatformPost post = entry.getValue();
                System.out.println("Creator: " + post.getCreator());
                System.out.println("Content: " + post.getContent());
                System.out.println(DIVIDER);
            }
        }
    }

    /**
     * Loads and displays user data and available actions.
     *
     * @param currentUser The current PlatformUser
     * @param scanner     Scanner object for user input
     * @param fd          FoundationDatabase object for data management
     */
    private static void loadUserData(PlatformUser currentUser, Scanner scanner, FoundationDatabase fd) {
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
                    viewUserPosts(currentUser);
                    valid = true;
                    break;
                case "2":
                    viewFriendsList(currentUser);
                    valid = true;
                    break;
                case "3":
                    viewFriendRequests(currentUser);
                    valid = true;
                    break;
                case "4":
                    addFriend(scanner, currentUser, fd);
                    valid = true;
                    break;
                default:
                    System.out.println("Error! Enter valid input.");
            }
        }
    }

    /**
     * Displays the user's posts.
     *
     * @param currentUser The current PlatformUser
     */
    private static void viewUserPosts(PlatformUser currentUser) {
        for (PlatformPost post : currentUser.getPosts()) {
            System.out.println(post.getContent());
            System.out.println(DIVIDER);
        }
    }

    /**
     * Displays the user's friends list.
     *
     * @param currentUser The current PlatformUser
     */
    private static void viewFriendsList(PlatformUser currentUser) {
        if (currentUser.getFriends().isEmpty()) {
            System.out.println("You have no friends.");
        } else {
            for (PlatformUser friend : currentUser.getFriends()) {
                System.out.println(friend.getUsername());
                System.out.println(DIVIDER);
            }
        }
    }

    /**
     * Displays and processes friend requests.
     *
     * @param currentUser The current PlatformUser
     */
    private static void viewFriendRequests(PlatformUser currentUser) {
        if (currentUser.getFriendRequests().isEmpty()) {
            System.out.println("No friend requests.");
        } else {
            for (PlatformFriendRequest request : currentUser.getFriendRequests()) {
                System.out.println("From: " + request.getUser());
                System.out.println("Message: " + request.getMessage());
                System.out.println("Accept? [Y/N]");
                System.out.println(DIVIDER);
                // Add logic to handle acceptance or rejection
            }
        }
    }

    /**
     * Adds a friend for the current user.
     *
     * @param scanner     Scanner object for user input
     * @param currentUser The current PlatformUser
     * @param fd          FoundationDatabase object for data management
     */
    private static void addFriend(Scanner scanner, PlatformUser currentUser, FoundationDatabase fd) {
        System.out.println("Enter their username:");
        String username = scanner.nextLine();
        System.out.println("Enter a message:");
        String message = scanner.nextLine();
        boolean success = PlatformFriendRequest.sendFriendRequest(username, currentUser.getUsername(), message, fd);

        if (success) {
            System.out.println("Friend request sent.");
        } else {
            System.out.println("Failed to send friend request.");
        }
    }

    /**
     * Handles post creation for the current user.
     *
     * @param scanner     Scanner object for user input
     * @param currentUser The current PlatformUser
     * @param fd          FoundationDatabase object for data management
     */
    private static void createNewPost(Scanner scanner, PlatformUser currentUser, FoundationDatabase fd) {
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
                    fd.addPost(new PlatformPost(currentUser.getUsername(), message));
                    System.out.println("Post uploaded successfully!");
                    return;
                case 2:
                    continue;
                case 3:
                    System.out.println("Operation cancelled.");
                    return;
            }
        }
    }

    /**
     * Logs the user out with a farewell message.
     */
    private static void logout() {
        System.out.println("Logging you out...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thank you for using Twitter 2.0");
    }
}
