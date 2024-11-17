import java.io.*;
import java.util.*;
import java.net.*;

/**
 * ExampleClient
 * 
 * @author Ropan Datta, L22
 * @version November 13, 2024
 */
public class ExampleClient {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        try (Socket socket = new Socket("localhost", 5000)) {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            for (int i = 0; i < 3; i++) {
                // The client only provides a username and password.
                // It doesn't know anything about the representation of user data
                // including user ids.
                // declares the next operation
                outputStream.writeObject(OperationType.CREATE_USER);
                // sends the username
                outputStream.writeObject(sc.nextLine());
                // sends the password
                outputStream.writeObject("world");
                outputStream.flush();
                // *important:* read in the boolean result of the operation!
                System.out.println(inputStream.readObject());
            }
            outputStream.writeObject(OperationType.LOGIN);
            outputStream.writeObject("hello");
            outputStream.writeObject("wrong_password");
            outputStream.flush();
            System.out.println(inputStream.readObject()); // false
            outputStream.writeObject(OperationType.LOGIN);
            outputStream.writeObject("hello");
            outputStream.writeObject("world"); // true - successfully logged in
            outputStream.flush();
            System.out.println(inputStream.readObject());
            outputStream.writeObject(OperationType.CREATE_POST); // would fail if user was not
                                                                 // logged in
            outputStream.writeObject("good morning!"); // string content
            outputStream.writeObject(null); // no image
            outputStream.flush();
            System.out.println(inputStream.readObject());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
