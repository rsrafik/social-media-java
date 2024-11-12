import java.io.*;
import java.net.*;

public class PlatformServer implements Runnable {
    int port;
    Database database;

    PlatformServer(int port) {
        this.port = port;
        database = new PlatformDatabase();
        database.readUsers();
        database.readPosts();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                System.out.println("Waiting for client to connect...");
                Socket socket = serverSocket.accept();
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.flush();
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("Client connected!");
                while (true) {
                    try {
                        OperationType operation = (OperationType) inputStream.readObject();
                        switch (operation) {
                            case LOGIN -> {

                            }
                            case CREATE_USER -> {
                                System.out.println("adding user");
                                String username = (String) inputStream.readObject();
                                // TODO: ensure that username is not taken
                                // String password =
                                // PlatformUser user = (PlatformUser) inputStream.readObject();
                                // System.out.println(user.getUserId());
                                // System.out.println(user.getUsername());
                                // System.out.println(user.postCount());
                                // database.addUser(user);
                            }
                            case CREATE_POST -> {
                                PlatformPost post = (PlatformPost) inputStream.readObject();
                                database.addPost(post);
                            }
                            case SEND_FRIENDREQUEST -> {

                            }
                            case TESTING -> {
                                System.out.println("testing");
                                String str = (String) inputStream.readObject();
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (EOFException e) {
                        break;
                    }
                }
                System.out.println("Client disconnected.");
                // write to files here??
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final int THREAD_COUNT = 6;
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(new PlatformServer(5678 + i));
            threads[i].start();
        }
        for (int i = 0; i < THREAD_COUNT; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
