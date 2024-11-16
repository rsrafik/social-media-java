import java.io.*;
import java.net.*;
import java.awt.*;

/**
 * PlatformServer
 * 
 * @author Ropan Datta, L22
 * @version November 13, 2024
 */

public class PlatformServer {
    public static void main(String[] args) throws IOException {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // TODO: keep track of number of active clients and set a limit on number of connections
            while (true) {
                System.out.println("Waiting for client to connect...");
                try {
                    Socket socket = serverSocket.accept();
                    Thread thread = new Thread(new ClientHandler(socket));
                    thread.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}


class ClientHandler implements Runnable {
    private static final String USERS_FILE = "users.dat";
    private static final String POSTS_FILE = "posts.dat";

    private static PlatformDatabase database;

    private Socket socket;

    public ClientHandler(Socket socket) {
        if (database == null) {
            database = new PlatformDatabase();
            try {
                // database.readUsers(USERS_FILE);
                // database.readPosts(POSTS_FILE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Client connected!");
            Integer loginId = null;
            while (true) {
                try {
                    OperationType operation = (OperationType) inputStream.readObject();
                    switch (operation) {
                        case IS_LOGGEDIN -> {
                            outputStream.writeObject(loginId != null);
                            outputStream.flush();
                        }
                        case LOGIN -> {
                            String username = (String) inputStream.readObject();
                            String password = (String) inputStream.readObject();
                            Integer userId = database.getUserId(username);
                            if (userId != null
                                    && database.getUser(userId).passwordEquals(password)) {
                                loginId = userId;
                                outputStream.writeObject(true);
                            } else {
                                outputStream.writeObject(false);
                            }
                            outputStream.flush();
                        }
                        case LOGOUT -> {
                            database.saveUsers(USERS_FILE);
                            database.savePosts(POSTS_FILE);
                            loginId = null;
                        }
                        // TODO: generate user ids in the server code
                        case CREATE_USER -> {
                            System.out.println("adding user");
                            String username = (String) inputStream.readObject();
                            // TODO: ensure that username is not taken
                            String password = (String) inputStream.readObject();
                            // TODO: ensure that password is good
                            PlatformUser user = new PlatformUser(0, username, password);
                            System.out.println(user.getUsername());
                            database.addUser(user);
                        }
                        case CREATE_POST -> {
                            if (loginId != null) {
                                String content = (String) inputStream.readObject();
                                Image image = (Image) inputStream.readObject();
                                // TODO: generate post id
                                PlatformPost post = new PlatformPost(0, loginId, content, image);
                                // PlatformPost post = (PlatformPost) inputStream.readObject();
                                database.addPost(post);
                                outputStream.writeObject(true);
                            } else {
                                outputStream.writeObject(false);
                            }
                            outputStream.flush();
                        }
                        case SEND_FRIENDREQUEST -> {
                            int toId = (Integer) inputStream.readObject();
                            if (loginId != null) {
                                // TODO
                                // need to check if `toId` is blocked, etc.
                                outputStream.writeObject(true); // placeholder
                            } else {
                                outputStream.writeObject(false);
                            }
                            outputStream.flush();
                        }
                        case TESTING -> {
                            // System.out.println("testing");
                            // String str = (String) inputStream.readObject();
                            Object lol = inputStream.readObject();
                            System.out.println(lol);
                        }
                    }
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Client disconnected.");
            // TODO: write to files here??
        }

    }
}
