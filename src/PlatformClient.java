import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class PlatformClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5002);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {


            outputStream.flush();
            boolean running = true;
            boolean loggedIn = false;

            while (running) {
                if (!loggedIn) {
                    // Login or Sign-Up Menu
                    System.out.println("1. Log In");
                    System.out.println("2. Sign Up");
                    System.out.println("3. Exit");
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1 -> {
                            // Log In
                            outputStream.writeObject(OperationType.LOGIN);
                            System.out.print("Enter username: ");
                            String username = scanner.nextLine();
                            outputStream.writeObject(username);
                            System.out.print("Enter password: ");
                            String password = scanner.nextLine();
                            outputStream.writeObject(password);
                            outputStream.flush();
                            loggedIn = (boolean) inputStream.readObject();
                            System.out.println(loggedIn ? "Login successful!" : "Login failed. Try again.");
                        }
                        case 2 -> {
                            // Sign Up
                            outputStream.writeObject(OperationType.CREATE_USER);
                            System.out.print("Enter new username: ");
                            String newUsername = scanner.nextLine();
                            outputStream.writeObject(newUsername);
                            System.out.print("Enter new password: ");
                            String newPassword = scanner.nextLine();
                            outputStream.writeObject(newPassword);
                            outputStream.flush();
                            boolean signedUp = (boolean) inputStream.readObject();
                            System.out.println(signedUp ? "User created successfully!" : "User creation failed. Try again.");
                            if (signedUp)
                                loggedIn = true;
                            if (!loggedIn)
                                break;

                            outputStream.writeObject(OperationType.LOGIN);
                            outputStream.writeObject(newUsername);
                            outputStream.writeObject(newPassword);
                            outputStream.flush();
                            loggedIn = (boolean) inputStream.readObject();
                            System.out.println(loggedIn ? "Login successful!" : "Login failed. Try again.");
                        }
                        case 3 -> {
                            // Exit
                            running = false;
                        }
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                } else {
                    // Logged-In Menu
                    System.out.println("\n1. Create Post");
                    System.out.println("2. Send Friend Request");
                    System.out.println("3. Log Out");
                    System.out.print("Choose an option: ");
                    int action = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (action) {
                        case 1 -> {
                            // Create Post
                            outputStream.writeObject(OperationType.CREATE_POST);
                            System.out.print("Enter post content: ");
                            String content = scanner.nextLine();
                            outputStream.writeObject(content);
                            outputStream.writeObject(null); // No image for simplicity
                            outputStream.flush();
                            boolean postResult = (boolean) inputStream.readObject();
                            System.out.println(postResult ? "Post created successfully!" : "Failed to create post.");
                        }
                        case 2 -> {
                            // Send Friend Request
                            outputStream.writeObject(OperationType.SEND_FRIENDREQUEST);
                            System.out.print("Enter user ID to send friend request: ");
                            int userId = scanner.nextInt();
                            outputStream.writeObject(userId);
                            outputStream.flush();
                            boolean friendRequestResult = (boolean) inputStream.readObject();
                            System.out.println(friendRequestResult ? "Friend request sent!" : "Failed to send friend request.");
                        }
                        case 3 -> {
                            // Log Out
                            outputStream.writeObject(OperationType.LOGOUT);
                            outputStream.flush();
                            boolean logOutResult = (boolean) inputStream.readObject();
                            if (logOutResult) {
                                loggedIn = false;
                                System.out.println("Logged out successfully!");
                            } else {
                                System.out.println("Failed to log out.");
                            }
                        }
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
