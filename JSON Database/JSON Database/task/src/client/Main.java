package client;

import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final String SERVER = "localhost";
    public static final int PORT = 19999;
    private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private static final String clientDataPath = System.getProperty("user.dir") + File.separator +
        "src" + File.separator +
        "client" + File.separator +
        "data";

    public static void main(String[] args) throws IOException {
        try {
            if (!Files.exists(Paths.get(clientDataPath))) {
                Files.createDirectories(Paths.get(clientDataPath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Args argv = new Args();
        JCommander.newBuilder()
                  .addObject(argv)
                  .build()
                  .parse(args);

        Gson gson = new GsonBuilder().create();
        String json = "{\"type\":\"" + argv.command + "\"," +
                    "\"key\":\"" + argv.key + "\"," +
                    "\"value\":\"" + argv.value + "\"}";
        if (argv.file != null) {
            json = new String(Files.readAllBytes(Paths.get(clientDataPath + "/" + argv.file)));
        }

        try (Socket socket = new Socket(SERVER, PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ){
            System.out.println("Client started!");
            System.out.println("Sent: " + json);
            output.writeUTF(json);
            System.out.println("Received: " + input.readUTF());
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
