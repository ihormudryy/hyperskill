package chat.utils;

import chat.server.Commands;
import chat.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;

public class Session implements Runnable {

    private DataInputStream input;
    private DataOutputStream output;
    private final Socket socket;
    private final Map<String, User> usersMainMap;
    private Optional<String> receiverUser = Optional.empty();
    private Optional<String> currentUser = Optional.empty();

    public Session(ServerSocket server, Map<String, User> usersMap) throws IOException {
        this.usersMainMap = usersMap;
        this.socket = server.accept();
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        if (usersMap.get(Server.ADMIN_NAME) == null) {
            Logger.info("Initializing admin to chat server.");
            usersMap.put(Server.ADMIN_NAME, new User(Server.ADMIN_NAME, this.output, Server.ADMIN_PASSWORD.hashCode()));
            usersMap.get(Server.ADMIN_NAME).setAsModerator(true);
            usersMap.get(Server.ADMIN_NAME).setOnline(false);
        }
    }

    private boolean connectionEstablished() {
        return usersMainMap.get(currentUser.get())
                .isConnectedWithUser(receiverUser.get()) &&
                usersMainMap.get(receiverUser.get())
                        .isConnectedWithUser(currentUser.get());
    }

    // Add new message to target user message list
    private void storeMessageHistory(String message) {
        Logger.info(String.format("Adding message \"%s\" to the lists of both users: %s -> %s.",
                message, currentUser.get(), receiverUser.get()));

        usersMainMap.get(receiverUser.get())
                .getMessages(currentUser.get())
                .add(new Message(message, connectionEstablished() && usersMainMap.get(receiverUser.get()).isOnline()));

        Logger.info("Sent from " + receiverUser.get() + " to " + currentUser.get() + " read: " + usersMainMap.get(receiverUser.get()).getReadMessages(currentUser.get()));
        Logger.info("New: " + usersMainMap.get(receiverUser.get()).getUnreadMessages(currentUser.get()));

        usersMainMap.get(currentUser.get())
                .getMessages(receiverUser.get())
                .add(new Message(message, usersMainMap.get(currentUser.get()).isOnline()));

        Logger.info("Sent from " + currentUser.get() + " to " + receiverUser.get() + " read: " + usersMainMap.get(currentUser.get()).getReadMessages(receiverUser.get()));
        Logger.info("New: " + usersMainMap.get(currentUser.get()).getUnreadMessages(receiverUser.get()));

        Logger.info(String.format("Sender %s is%s online, receiver %s is%s online and mutual connection is %s.",
                currentUser.get(), usersMainMap.get(currentUser.get()).isOnline() ? "" : " NOT",
                receiverUser.get(), usersMainMap.get(receiverUser.get()).isOnline() ? "" : " NOT", connectionEstablished()));
    }

    // Send message to receiver only if we have established mutual connection
    private void broadcastMessages(String message) {
        try {
            Logger.info("Broadcasting message " + message);
            if (connectionEstablished()) {
                usersMainMap.get(currentUser.get())
                        .getOpenChatsByUserName(receiverUser.get())
                        .getOutput()
                        .writeUTF(message);
            } else {
                Logger.info("No mutual connection between " + currentUser.get() + " and " + receiverUser.get());
            }
            // Send message to myself...
            usersMainMap.get(currentUser.get())
                    .getOutput()
                    .writeUTF(message);
        } catch (IOException e) {
            Logger.info("Something went wrong with message broadcast.");
        }
    }

    @Override
    public void run() {
        try {
            Responses.AUTH_OR_REGISTER.send(output);
            while (true) {
                try {
                    String request = input.readUTF();
                    ArgumentsWrapper args = new ArgumentsWrapper();
                    args.setOutput(output)
                            .setCurrentUser(currentUser)
                            .setReceiverUser(receiverUser)
                            .setUsersMainMap(usersMainMap)
                            .setRequest(request);

                    if (Commands.REGISTER.isFunction(request)) {
                        currentUser = Commands.REGISTER.execute(args);
                        continue;
                    } else if (Commands.AUTH.isFunction(request)) {
                        currentUser = Commands.AUTH.execute(args);
                        continue;
                    } else if (Commands.LIST.isFunction(request)) {
                        Commands.LIST.execute(args);
                        continue;
                    } else if (Commands.CHAT.isFunction(request)) {
                        receiverUser = Commands.CHAT.execute(args);
                        if (receiverUser.isPresent()) {
                            usersMainMap.get(currentUser.get())
                                    .addNewOpenChat(receiverUser.get(), usersMainMap.get(receiverUser.get()));
                        }
                        continue;
                    } else if (Commands.EXIT.isFunction(request)) {
                        Commands.EXIT.execute(args);
                        continue;
                    } else if (Commands.UNREAD.isFunction(request)) {
                        Commands.UNREAD.execute(args);
                        continue;
                    } else if (Commands.KICK.isFunction(request)) {
                        Commands.KICK.execute(args);
                        continue;
                    } else if (Commands.GRANT.isFunction(request)) {
                        Commands.GRANT.execute(args);
                        continue;
                    } else if (Commands.REVOKE.isFunction(request)) {
                        Commands.REVOKE.execute(args);
                        continue;
                    } else if (Commands.HISTORY.isFunction(request)) {
                        Commands.HISTORY.execute(args);
                        continue;
                    } else if (Commands.STATS.isFunction(request)) {
                        Commands.STATS.execute(args);
                        continue;
                    } else if (Commands.UNKNOWN.isFunction(request)) {
                        Commands.UNKNOWN.execute(args);
                        continue;
                    }

                    // Send not in chat message is current user is not authorised or registered
                    if (!currentUser.isPresent() || usersMainMap.get(currentUser.get()).isBanned()) {
                        Responses.USER_NOT_IN_CHAT.send(output);
                        continue;
                    }

                    // Send hint message to use /list command to see who is online
                    if (!receiverUser.isPresent()) {
                        Responses.HINT_TO_USE_LIST.send(output);
                        continue;
                    }

                    // Broadcast chat message to target users
                    if (usersMainMap.get(receiverUser.get()) != null) {
                        Logger.info("From " + currentUser.get() + " => " + receiverUser.get());
                        String fullMessage = currentUser.get() + ": " + request;
                        broadcastMessages(fullMessage);
                        storeMessageHistory(fullMessage);
                    } else {
                        Logger.info(receiverUser.get() + " does not exist!");
                        Responses.HINT_TO_USE_LIST.send(output);
                    }
                } catch (EOFException ex) {
                    // Reestablish stream connectors
                    this.input = new DataInputStream(socket.getInputStream());
                    this.output = new DataOutputStream(socket.getOutputStream());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
