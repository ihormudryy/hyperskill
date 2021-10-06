package chat.server;

import chat.utils.Logger;
import chat.utils.Session;
import chat.utils.User;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static chat.config.Configuration.LOCALHOST;

public class Server {

    public static final String ADMIN_NAME = "admin";
    public static final String ADMIN_PASSWORD = "12345678";
    public static final int MAX_TOTAL_OUTPUTS = 25;
    public static final int MAX_TOTAL_READ_OUTPUTS = 10;
    private static final String USERS_SERIALIZED_FILE = "./../../users.txt";
    private static final int SERVER_TIMEOUT = 3000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(LOCALHOST.getPort(), 50,
                InetAddress.getByName(LOCALHOST.getIp()))) {
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime()
                    .availableProcessors());
            Map<String, User> usersMap;
            if (new File(USERS_SERIALIZED_FILE).exists()) {
                FileInputStream fileInputStream = new FileInputStream(USERS_SERIALIZED_FILE);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                usersMap = (Map<String, User>) objectInputStream.readObject();
                objectInputStream.close();
                Logger.info("Deserialized users from file!");
            } else {
                usersMap = new HashMap<>();
                Logger.info("Created new users hash map!");
            }
            serverSocket.setSoTimeout(SERVER_TIMEOUT);
            while (true) {
                try {
                    executor.execute(new Session(serverSocket, usersMap));
                } catch (Exception e) {
                    e.printStackTrace();
                    serverSocket.close();
                    executor.shutdown();
                    FileOutputStream fileOutputStream
                            = new FileOutputStream(USERS_SERIALIZED_FILE);
                    ObjectOutputStream objectOutputStream
                            = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(usersMap);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    Logger.info("Server stopped!");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.info("Server connection refused!");
        }
    }
}
