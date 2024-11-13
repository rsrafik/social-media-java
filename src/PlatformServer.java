import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.image.*;

public class PlatformServer implements Runnable {
    private static final String USERS_FILE = "users.dat";
    private static final String POSTS_FILE = "posts.dat";

    private static PlatformDatabase database;

    private int port;

    PlatformServer(int port) {
        if (database == null) {
            database = new PlatformDatabase();
            try {
                // database.readUsers(USERS_FILE);
                // database.readPosts(POSTS_FILE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.port = port;
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
                                // PlatformUser user = database.getUserByUsername(username);
                                Integer userId = database.getUserId(username);
                                if (userId != null
                                        && database.getUser(userId).testPassword(password)) {
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
                            case CREATE_USER -> {
                                System.out.println("adding user");
                                String username = (String) inputStream.readObject();
                                // TODO: ensure that username is not taken
                                String password = (String) inputStream.readObject();
                                // TODO: ensure that password is good
                                PlatformUser user = new PlatformUser(username, password);
                                database.addUser(user);
                            }
                            case CREATE_POST -> {
                                if (loginId != null) {
                                    String content = (String) inputStream.readObject();
                                    BufferedImage image = (BufferedImage) inputStream.readObject();
                                    PlatformPost post = new PlatformPost(loginId, content, image);
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
                System.out.println("Client disconnected.");
                // TODO: write to files here??
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            for (int i = 0; i < 6; i++) {
                System.out.println("Waiting for client to connect...");
                try {
                    Socket socket = serverSocket.accept();
                    new Thread(new ClientHandler(socket)).start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}


/**
 * Just an example not actually used
 */

class ClientHandler implements Runnable {
    Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connected!");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject("Hii!!");
            oos.flush();
            while (true) {
                try {
                    Object obj = ois.readObject();
                    System.out.println(obj);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Client disconnected.");
        }
    }
}
