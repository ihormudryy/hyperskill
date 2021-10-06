package chat.utils;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, List<Message>> messages;
    private transient HashMap<String, User> openChats;
    private final String username;
    private final int passwordHash;
    private transient boolean online;
    private transient DataOutputStream output;
    private LocalDateTime banUntil;
    private boolean isModerator;

    public User(String name,
                DataOutputStream output,
                int passwordHash) {
        this.username = name;
        this.passwordHash = passwordHash;
        this.messages = new HashMap<>();
        this.openChats = new HashMap<>();
        this.output = output;
        this.online = true;
        this.banUntil = LocalDateTime.now();
        this.isModerator = false;
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject();
        online = false;
        openChats = new HashMap<>();
    }

    public void kickUserFromChat() {
        this.online = false;
        this.banUntil = LocalDateTime.now().plus(5, ChronoUnit.MINUTES);
    }

    public boolean isBanned() {
        return banUntil.isAfter(LocalDateTime.now());
    }

    public void setAsModerator(boolean flag) {
        this.isModerator = flag;
    }

    public boolean isModerator() {
        return isModerator;
    }

    public List<String> getUsersWithUnreadMessages() {
        return messages.keySet()
                .stream()
                .filter(k -> messages.get(k).stream().filter(m -> m.isNew()).count() > 0)
                .collect(Collectors.toList());
    }

    public List<String> getReadMessages(String username) {
        if (messages.get(username) != null) {
            return messages.get(username)
                    .stream()
                    .filter(v -> !v.isNew())
                    .map(m -> m.getContent())
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    public List<String> getUnreadMessages(String username) {
        if (messages.get(username) != null) {
            return messages.get(username)
                    .stream()
                    .filter(v -> v.isNew())
                    .map(m -> m.getContent())
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    public List<String> getUnreadMessagesAndMarkAsRead(String username) {
        if (messages.get(username) != null) {
            return messages.get(username)
                    .stream()
                    .filter(v -> v.isNew())
                    .map(m -> {
                        m.setRead();
                        return m.getContent();
                    })
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    public boolean isPasswordValid(String password) {
        return this.passwordHash == password.hashCode();
    }

    public boolean isConnectedWithUser(String username) {
        return this.openChats.containsKey(username);
    }

    public void addNewOpenChat(String name, User user) {
        this.openChats.put(name, user);
    }

    public User getOpenChatsByUserName(String name) {
        return this.openChats.get(name);
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getUsername() {
        return username;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    public void setOutput(DataOutputStream output) {
        this.output = output;
    }

    public List<Message> getMessages(String username) {
        if (messages.get(username) == null) {
            messages.put(username, new LinkedList<>());
        }
        return messages.get(username);
    }

    public List<String> getMessagesFrom(String username) {
        if (messages.get(username) != null) {
            return messages.get(username)
                    .stream()
                    .filter(v -> v.getContent().startsWith(username))
                    .map(m -> m.getContent())
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }
}
