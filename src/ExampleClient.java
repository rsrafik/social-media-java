import java.io.*;
import java.net.*;

/**
 * ExampleClient
 */
public class ExampleClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 5678)) {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            for (int i = 0; i < 10; i++) {
                PlatformUser user = new PlatformUser("user" + i, "world");
                outputStream.writeObject(OperationType.ADD_USER);
                outputStream.writeObject(user);
                outputStream.flush();
            }
            // PlatformUser user = new PlatformUser("hello", "world");
            // System.out.println(user.getUserId());
            // System.out.println(user.getUsername());
            // System.out.println(user.postCount());
            // outputStream.writeObject(OperationType.ADD_USER);
            // outputStream.writeObject(user);
            // System.out.println(user.getUserId());
            // System.out.println(user.getUsername());
            // System.out.println(user.postCount());
            // // PlatformUser user2 = (PlatformUser) inputStream.readObject();
        }
    }
}
