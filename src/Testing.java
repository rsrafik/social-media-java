//import java.io.*;
//
///**
// * Testing
// * <p>
// * This class tests the functionality of the FoundationDatabase by adding 100 sample users,
// * saving them to a file, and then reading the file back to display the users.
// * If the file "users.dat" already exists, it is deleted at the start to ensure a fresh test.
// *
// * @author Rachel Rafik
// * @version November 1, 2024
// */
//public class Testing {
//
//    /**
//     * The main method for testing the FoundationDatabase functionality.
//     * It deletes any existing "users.dat" file, adds 100 new users, saves them,
//     * and then reads back all users to verify they were saved correctly.
//     *
//     * @param args Command-line arguments (not used)
//     * @throws InterruptedException if thread synchronization is interrupted
//     */
//    public static void main(String[] args) throws InterruptedException {
//        File file = new File("users.dat");
//
//        // Delete "users.dat" if it exists to start fresh
//        if (file.exists()) {
//            file.delete();
//        }
//
//        FoundationDatabase fd = new FoundationDatabase();
//
//        // Read any existing users (expected to be none due to deletion above)
//        fd.readUsers();
//
//        // Add 100 sample users with unique usernames and passwords
//        for (int i = 1; i <= 100; i++) {
//            fd.addUser(new PlatformUser("User" + i, "Password" + i));
//        }
//
//        // Display the contents of "users.dat" after additions
//        System.out.println("Contents of users.dat after additions:");
//
//        // Read and display each user from "users.dat" to verify data integrity
//        try (FileInputStream fileIn = new FileInputStream("users.dat");
//             ObjectInputStream in = new ObjectInputStream(fileIn)) {
//
//            while (true) {
//                try {
//                    User user = (User) in.readObject(); // Deserialize User object
//                    System.out.println("User ID: " + user.getUserId() + ", Username: " + user.getUsername());
//                } catch (EOFException e) {
//                    break; // End of file reached
//                }
//            }
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//            System.out.println("Error with reading file");
//        }
//    }
//}
