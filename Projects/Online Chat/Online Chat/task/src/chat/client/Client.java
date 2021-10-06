package chat.client;

import chat.utils.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import static chat.config.Configuration.LOCALHOST;

public class Client {

    public static void main(String[] args) throws Exception {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket(InetAddress.getByName(LOCALHOST.getIp()), LOCALHOST.getPort());
            } catch (IOException e) {

            }
        }
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        Scanner sc = new Scanner(System.in);
        Logger.info("Client started!");
        Socket finalSocket = socket;
        new Thread(() -> {
            String serverMsg;
            try {
                while (finalSocket.isConnected()) {
                    serverMsg = input.readUTF();
                    if (!serverMsg.isEmpty()) {
                        System.out.println(serverMsg);
                    }
                }
            } catch (IOException e) {
            }
        }).start();
        while (true) {
            try {
                String clientMsg = sc.nextLine();
                output.writeUTF(clientMsg);
                if ("/exit".equals(clientMsg)) {
                    socket.close();
                    System.exit(0);
                }
            } catch (SocketException e) {
                output = new DataOutputStream(socket.getOutputStream());
            }
        }
    }
}
