package chat.server;

import chat.utils.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public enum Commands implements Command {
    REGISTER("/registration") { // Register new user

        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            Map<String, String> creds = parseRequest(args.getRequest());
            if (args.getUsersMainMap().get(creds.get("name")) != null) {
                try {
                    Responses.LOGIN_IS_ALREADY_TAKEN.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (creds.get("password").length() < 8) {
                try {
                    Responses.PASSWORD_TOO_SHORT.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            User currentUser = new User(creds.get("name"), args.getOutput(), creds.get("password").hashCode());
            args.getUsersMainMap().put(creds.get("name"), currentUser);
            try {
                Responses.REGISTERED_SUCCESSFULLY.send(args.getOutput());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Optional.of(currentUser.getUsername());
        }
    },
    AUTH("/auth") { // Authenticate user

        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            Map<String, String> creds = parseRequest(args.getRequest());

            if (args.getUsersMainMap().get(creds.get("name")) == null) {
                try {
                    Responses.INCORRECT_LOGIN.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (!args.getUsersMainMap().get(creds.get("name")).isPasswordValid(creds.get("password"))) {
                try {
                    Responses.INCORRECT_PASSWORD.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (args.getUsersMainMap().get(creds.get("name")).isBanned()) {
                try {
                    Responses.USER_IS_BANNED.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                Responses.AUTHORIZED_SUCCESSFULLY.send(args.getOutput());
            } catch (IOException e) {
                e.printStackTrace();
            }

            args.getUsersMainMap().get(creds.get("name")).setOutput(args.getOutput());
            args.getUsersMainMap().get(creds.get("name")).setOnline(true);
            return Optional.of(creds.get("name"));
        }
    },
    LIST("/list") { // List users online

        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            // Check if user is banned
            if (args.getUsersMainMap().get(args.getCurrentUser().get()).isBanned()) {
                try {
                    Responses.USER_NOT_IN_CHAT.send(args.getOutput());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                String usernames = args.getUsersMainMap().entrySet().stream()
                        .filter(e -> e.getValue().isOnline()
                                && !e.getValue().getUsername().equals(args.getCurrentUser().get()))
                        .map(e -> e.getValue().getUsername())
                        .reduce("", (a, v) -> a + " " + v);
                if (usernames.length() == 0) {
                    Responses.NO_ONLINE.send(args.getOutput());
                } else {
                    Responses.ONLINE.send(args.getOutput(), usernames);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        }
    },
    CHAT("/chat") {
        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            Map<String, String> creds = parseRequest(args.getRequest());
            if (args.getUsersMainMap().get(creds.get("name")) == null) {
                try {
                    Responses.USER_IS_NOT_ONLINE.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Logger.info("Fetching messages for " + creds.get("name"));
                List<String> readMessages = args.getUsersMainMap().get(args.getCurrentUser().get()).getReadMessages(creds.get("name"));
                List<String> unreadMessages = args.getUsersMainMap().get(args.getCurrentUser().get()).getUnreadMessagesAndMarkAsRead(creds.get("name"));
                int s1 = unreadMessages.size();
                int s2 = readMessages.size();
                Logger.info(String.format("Messages %s -> %s new: %s, read: %s.", args.getCurrentUser().get(), creds.get("name"), s1, s2));
                if (s1 < Server.MAX_TOTAL_OUTPUTS) {
                    IntStream.range(s2 - Math.min(Server.MAX_TOTAL_READ_OUTPUTS, s2), s2)
                            .forEach(i -> {
                                try {
                                    args.getUsersMainMap().get(args.getCurrentUser().get())
                                            .getOutput()
                                            .writeUTF(readMessages.get(i));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                }
                IntStream.range(s1 - Math.min(Server.MAX_TOTAL_OUTPUTS - Math.min(Server.MAX_TOTAL_READ_OUTPUTS, s2), s1), s1)
                        .forEach(i -> {
                            try {
                                args.getUsersMainMap().get(args.getCurrentUser().get())
                                        .getOutput()
                                        .writeUTF(String.format("(new) %s", unreadMessages.get(i)));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

            }
            return Optional.of(creds.get("name"));
        }
    },
    EXIT("/exit") {
        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            try {
                args.getUsersMainMap().get(args.getCurrentUser().orElseThrow()).setOnline(false);
                args.getOutput().writeUTF("/exit");
                Logger.info(args.getCurrentUser().get() + " closed");
                args.getOutput().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        }
    },
    UNKNOWN("/") {
        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            try {
                Responses.INCORRECT_COMMAND.send(args.getOutput());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        }
    },
    KICK("/kick") {
        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            String userToKickOut = parseRequest(args.getRequest()).get("name");
            if (args.getCurrentUser().get().equals(userToKickOut)) {
                try {
                    Responses.USER_CANT_KICK_THEMSELF.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (!args.getUsersMainMap().get(args.getCurrentUser().get()).isModerator()) {
                try {
                    Responses.USER_IS_NOT_A_MODERATOR_OR_AN_ADMIN.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Logger.info("Kicking out \"" + userToKickOut + "\" on behalf of \"" + args.getCurrentUser().get() + "\"");
            if (// IF user is moderator AND not admin AND receiver is NOT moderator then kick him out
                    (!userToKickOut.equals(Server.ADMIN_NAME)
                            && args.getUsersMainMap().get(args.getCurrentUser().get()).isModerator()
                            && !args.getUsersMainMap().get(userToKickOut).isModerator()) ||
                            // Or if this is ADMIN
                            args.getCurrentUser().get().equals(Server.ADMIN_NAME)) {

                try {
                    args.getUsersMainMap().get(userToKickOut).kickUserFromChat();
                    Responses.USER_HAS_BEEN_KICKED.sendWithValues(args.getOutput(), userToKickOut); // Server message
                    Responses.YOU_HAVE_BEEN_KICKED_OUT.send(args.getUsersMainMap().get(userToKickOut).getOutput()); // Personal message
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Logger.info("Kicked " + userToKickOut + " out of chat!");
            } else {
                Logger.info("Failed to kick " + userToKickOut + " out of chat!");
            }
            return Optional.empty();
        }
    },
    GRANT("/grant") {
        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            String targetUser = parseRequest(args.getRequest()).get("name");

            if (args.getUsersMainMap().get(targetUser).isModerator()) {
                try {
                    Responses.USER_IS_ALREADY_A_MODERATOR.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (!args.getCurrentUser().get().equals(Server.ADMIN_NAME)) {
                try {
                    Responses.USER_IS_NOT_AN_ADMIN.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // If user is admin then grant receiver moderator rights
            if (args.getCurrentUser().get().equals("admin")
                    && args.getUsersMainMap().get(targetUser) != null) {
                args.getUsersMainMap().get(targetUser).setAsModerator(true);
                try {
                    Responses.USER_IS_A_NEW_MODERATOR.sendWithValues(args.getOutput(), targetUser); // Server message
                    Responses.USER_IS_MODERATOR_NOW.send(args.getUsersMainMap().get(targetUser).getOutput());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Logger.info("Set " + targetUser + " as moderator!");
            } else {
                Logger.info("Failed to set " + targetUser + " as moderator!");
            }
            return Optional.empty();
        }
    },
    REVOKE("/revoke") {
        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            String targetUser = parseRequest(args.getRequest()).get("name");

            if (!args.getCurrentUser().get().equals(Server.ADMIN_NAME)) {
                try {
                    Responses.USER_IS_NOT_AN_ADMIN.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (!args.getUsersMainMap().get(targetUser).isModerator()) {
                try {
                    Responses.USER_IS_NOT_A_MODERATOR.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // If user is admin and receiver is not admin
            if (args.getUsersMainMap().get(targetUser) != null
                    && !targetUser.equals("admin")) {
                try {
                    args.getUsersMainMap().get(targetUser).setAsModerator(false);
                    Responses.USER_IS_NO_LONGER_A_MODERATOR.sendWithValues(args.getOutput(), targetUser);
                    Responses.USER_IS_NOT_A_MODERATOR_ANY_MORE.send(args.getUsersMainMap().get(targetUser).getOutput());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Logger.info("Revoked from " + targetUser + " moderator status!");
            } else {
                Logger.info("Failed to revoke from " + targetUser + " moderator status!");
            }
            return Optional.empty();
        }
    },
    UNREAD("/unread") {
        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            List<String> users = args.getUsersMainMap().get(args.getCurrentUser().get()).getUsersWithUnreadMessages();
            Collections.sort(users);
            Logger.info("Fetched users " + users.size() + " with unread messages for " + args.getCurrentUser().get());
            if (users.isEmpty()) {
                try {
                    Responses.SERVER_NO_ONE_UNREAD.send(args.getOutput());
                    return Optional.empty();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                Responses.SERVER_UNREAD_FROM.sendWithValues(args.getOutput(), String.join(" ", users));
                return Optional.empty();
            } catch (IOException e) {
                Logger.info("Failed to fetch user names with unread messages for " + args.getCurrentUser().get());
                e.printStackTrace();
            } finally {
                return Optional.empty();
            }
        }
    },
    HISTORY("/history") {
        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            String value = parseRequest(args.getRequest()).get("name");
            try {
                Integer num = Integer.valueOf(value);
                Logger.info("Getting history for {" + args.getCurrentUser().get() + "} with messages " + num);
                List<Message> messages = args.getUsersMainMap()
                        .get(args.getCurrentUser().get())
                        .getMessages(args.getReceiverUser().get());
                int s1 = messages.size();
                try {
                    args.getOutput().writeUTF("Server:");
                    for (int i = Math.max(s1 - num, 0); i < Math.min(25 + Math.max(s1 - num, 0), s1); i++) {
                        args.getOutput().writeUTF(String.format("%s%s", messages.get(i).isNew() ? "(new) " : "",
                                messages.get(i).getContent()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NumberFormatException e) {
                Responses.IS_NOT_A_NUMBER.sendWithValues(args.getOutput(), value);
            } finally {
                return Optional.empty();
            }
        }
    },
    STATS("/stats") {
        @Override
        public Optional<String> execute(ArgumentsWrapper args) {
            User currentUser = args.getUsersMainMap().get(args.getCurrentUser().get());
            User targetUser = args.getUsersMainMap().get(args.getReceiverUser().get());
            try {
                Logger.info("Fetching stats between {" + args.getCurrentUser().get() +
                        "} with {" + args.getReceiverUser().get() + "}");
                String stats = String.format("Server:\nStatistics with %s:\n" +
                                "Total messages: %s\n" +
                                "Messages from %s: %s\n" +
                                "Messages from %s: %s",
                        args.getReceiverUser().get(), currentUser.getMessages(args.getReceiverUser().get()).size(),
                        args.getCurrentUser().get(), targetUser.getMessagesFrom(args.getCurrentUser().get()).size(),
                        args.getReceiverUser().get(), currentUser.getMessagesFrom(args.getReceiverUser().get()).size());
                args.getOutput().writeUTF(stats);
            } catch (IOException e) {
                Logger.info("Failed to fetch stats between {" + args.getCurrentUser().get() +
                        "} with {" + args.getReceiverUser().get() + "}");
                e.printStackTrace();
            }
            return Optional.empty();
        }
    };

    public String function;

    Commands(final String function) {
        this.function = function;
    }

    public Map<String, String> parseRequest(String request) {
        String[] credentials = request.split("\\s+");
        Map<String, String> result = new HashMap<>();
        result.put("name", credentials[1]);
        if (credentials.length > 2) {
            result.put("password", credentials[2]);
        }
        return result;
    }

    public boolean isFunction(String s) {
        return s.contains(this.function);
    }
}
