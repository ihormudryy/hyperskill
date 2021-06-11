package server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Session implements Runnable {
    private ServerSocket server;
    public static boolean exitFlag = false;
    public DataBaseManager db;
    private Socket socket;

    public Session(ServerSocket server,
                   DataBaseManager db) throws IOException {
        try {
            this.server = server;
            this.db = db;
            socket = server.accept();
        } catch (Exception e) {
        }
    }

    public static String fullResponse(String code, String reason, String value) {
        String response = "{ \"response\": \"%s\", \"%s\": %s }";
        return String.format(response, code, reason, value);
    }

    public static String shortResponse(String code) {
        String response = "{ \"response\": \"%s\" }";
        return String.format(response, code);
    }

    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        while (!this.exitFlag) {
            try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {
                String request = input.readUTF();
                JsonNode jn = mapper.readTree(request);
                switch (jn.get("type").asText()) {
                    case "get": {
                        try {
                            output.writeUTF(fullResponse("OK", "value", db.get(jn)));
                        } catch (Exception e) {
                            output.writeUTF(fullResponse("ERROR", "reason", "\"No such key\""));
                        }
                        break;
                    }
                    case "set": {
                        db.set(jn);
                        output.writeUTF(shortResponse("OK"));
                        break;
                    }
                    case "delete": {
                        try {
                            db.delete(jn);
                            output.writeUTF(shortResponse("OK"));
                        } catch (Exception e) {
                            output.writeUTF(fullResponse("ERROR", "reason", "\"No such key\""));
                        }
                        break;
                    }
                    case "exit": {
                        this.exitFlag = true;
                        output.writeUTF(shortResponse("OK"));
                        output.close();
                        input.close();
                        socket.close();
                        server.close();
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {
    private static int PORT = 19999;
    private static String SERVER = "localhost";
    private static int numberOfThreads = 20;

    public static void main(String[] args) throws IOException {
        DataBaseManager db = new DataBaseManager();

        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER))) {
            ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
            System.out.println("Server started!");
            while(true) {
                if (!Session.exitFlag) {
                    try {
                        //Session clientSocket = new Session(server, db);
                        executor.submit(new Session(server, db));
                    } catch (Exception e) {
                        server.close();
                        executor.shutdown();
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Great success!");
        }
    }
}
