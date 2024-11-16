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
            // outputStream.writeObject(OperationType.TESTING);
            // outputStream.writeObject(true);
            // outputStream.writeObject(OperationType.TESTING);
            // outputStream.writeObject("Hello");
            // outputStream.writeObject(OperationType.TESTING);
            // outputStream.writeObject(Boolean.TRUE);
            // outputStream.flush();
            for (int i = 0; i < 10; i++) {
                // The client only provides a username and password.
                // It doesn't know anything about the representation of user data
                // including user ids.
                outputStream.writeObject(OperationType.CREATE_USER);
                outputStream.writeObject(sc.nextLine());
                outputStream.writeObject("world");
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
