import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Platform Client
 *
 * This program acts as a client for connecting to a server via socket.
 * It provides functionalities for users to log in, sign up, create posts,
 * send friend requests, and log out. Communication with the server is done
 * through serialized objects over the network.
 *
 * @author Rachel Rafik
 * @version November 17, 2024
 */
public class PlatformClient {

    public static void main(String[] args) {
        // Try-with-resources to ensure proper closing of streams and sockets
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            outputStream.flush();  // Ensure ObjectOutputStream is properly initialized
            boolean running = true;  // Flag to control main loop
            boolean loggedIn = false;  // Flag to track login status

            while (running) {
                if (!loggedIn) {
                    // Display login or sign-up menu
                    System.out.println("1. Log In");
                    System.out.println("2. Sign Up");
                    System.out.println("3. Exit");
                    System.out.print("Choose an option: ");
                    String choice = scanner.nextLine();

                    switch (choice) {
                        case "1" -> handleLogin(outputStream, inputStream, scanner);
                        case "2" -> loggedIn = handleSignUp(outputStream, inputStream, scanner);
                        case "3" -> running = false;  // Exit application
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                } else {
                    // Display menu for logged-in users
                    System.out.println("\n1. Create Post");
                    System.out.println("2. Send Friend Request");
                    System.out.println("3. Log Out");
                    System.out.print("Choose an option: ");
                    String action = scanner.nextLine();

                    switch (action) {
                        case "1" -> handleCreatePost(outputStream, inputStream, scanner);
                        case "2" -> handleFriendRequest(outputStream, inputStream, scanner);
                        case "3" -> loggedIn = handleLogout(outputStream, inputStream);
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace for exceptions
        }
    }

    /**
     * Handles the login operation for the client.
     *
     * @param outputStream the output stream to send data to the server
     * @param inputStream the input stream to receive data from the server
     * @param scanner the Scanner to read user input
     */
    private static void handleLogin(ObjectOutputStream outputStream, ObjectInputStream inputStream, Scanner scanner)
            throws IOException, ClassNotFoundException {
        outputStream.writeObject(OperationType.LOGIN);  // Request login operation
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        outputStream.writeObject(username);
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        outputStream.writeObject(password);
        outputStream.flush();

        boolean loggedIn = (boolean) inputStream.readObject();  // Receive login result
        System.out.println(loggedIn ? "Login successful!" : "Login failed. Try again.");
    }

    /**
     * Handles the sign-up operation for the client.
     *
     * @param outputStream the output stream to send data to the server
     * @param inputStream the input stream to receive data from the server
     * @param scanner the Scanner to read user input
     * @return true if the user is successfully logged in after sign-up, false otherwise
     */
    private static boolean handleSignUp(ObjectOutputStream outputStream, ObjectInputStream inputStream, Scanner scanner)
            throws IOException, ClassNotFoundException {
        outputStream.writeObject(OperationType.CREATE_USER);  // Request user creation
        System.out.print("Enter new username: ");
        String newUsername = scanner.nextLine();
        outputStream.writeObject(newUsername);
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        outputStream.writeObject(newPassword);
        outputStream.flush();

        boolean signedUp = (boolean) inputStream.readObject();  // Receive sign-up result
        System.out.println(signedUp ? "User created successfully!" : "User creation failed. Try again.");

        if (signedUp) {
            // Auto login after successful sign-up
            outputStream.writeObject(OperationType.LOGIN);
            outputStream.writeObject(newUsername);
            outputStream.writeObject(newPassword);
            outputStream.flush();
            return (boolean) inputStream.readObject();  // Receive login result
        }
        return false;
    }

    /**
     * Handles the creation of a post for the logged-in user.
     *
     * @param outputStream the output stream to send data to the server
     * @param inputStream the input stream to receive data from the server
     * @param scanner the Scanner to read user input
     */
    private static void handleCreatePost(ObjectOutputStream outputStream, ObjectInputStream inputStream,
                                         Scanner scanner) throws IOException, ClassNotFoundException {
        outputStream.writeObject(OperationType.CREATE_POST);  // Request post creation
        System.out.print("Enter post content: ");
        String content = scanner.nextLine();
        outputStream.writeObject(content);
        outputStream.writeObject(null);  // No image for simplicity
        outputStream.flush();

        boolean postResult = (boolean) inputStream.readObject();  // Receive post result
        System.out.println(postResult ? "Post created successfully!" : "Failed to create post.");
    }

    /**
     * Handles sending a friend request.
     *
     * @param outputStream the output stream to send data to the server
     * @param inputStream the input stream to receive data from the server
     * @param scanner the Scanner to read user input
     */
    private static void handleFriendRequest(ObjectOutputStream outputStream, ObjectInputStream inputStream,
                                            Scanner scanner) throws IOException, ClassNotFoundException {
        outputStream.writeObject(OperationType.SEND_FRIENDREQUEST);  // Request friend request operation
        System.out.print("Enter user ID to send friend request: ");
        int userId = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        outputStream.writeObject(userId);
        outputStream.flush();

        boolean friendRequestResult = (boolean) inputStream.readObject();  // Receive result
        System.out.println(friendRequestResult ? "Friend request sent!" : "Failed to send friend request.");
    }

    /**
     * Handles the logout operation for the client.
     *
     * @param outputStream the output stream to send data to the server
     * @param inputStream the input stream to receive data from the server
     * @return true if logged out successfully, false otherwise
     */
    private static boolean handleLogout(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        outputStream.writeObject(OperationType.LOGOUT);  // Request logout operation
        outputStream.flush();

        boolean logOutResult = (boolean) inputStream.readObject();  // Receive logout result
        if (logOutResult) {
            System.out.println("Logged out successfully!");
            return false;  // User is no longer logged in
        } else {
            System.out.println("Failed to log out.");
            return true;  // Remain logged in if logout fails
        }
    }
}
