package chat.utils;

import java.io.DataOutputStream;
import java.io.IOException;

public enum Responses {

    INCORRECT_LOGIN("Server: incorrect login!"),
    USER_IS_BANNED("Server: you are banned!"),
    INCORRECT_PASSWORD("Server: incorrect password!"),
    PASSWORD_TOO_SHORT("Server: the password is too short!"),
    LOGIN_IS_ALREADY_TAKEN("Server: this login is already taken! Choose another one."),
    USER_IS_NOT_ONLINE("Server: the user is not online!"),
    INCORRECT_COMMAND("Server: incorrect command!"),
    AUTH_OR_REGISTER("Server: authorize or register"),
    REGISTERED_SUCCESSFULLY("Server: you are registered successfully!"),
    AUTHORIZED_SUCCESSFULLY("Server: you are authorized successfully!"),
    HINT_TO_USE_LIST("Server: use /list command to choose a user to text!"),
    USER_NOT_IN_CHAT("Server: you are not in the chat!"),
    ONLINE("Server: online:"),
    NO_ONLINE("Server: no one online"),
    USER_CANT_KICK_THEMSELF("Server: you can't kick yourself!"),
    USER_HAS_BEEN_KICKED("Server: %s was kicked!"),
    USER_IS_NOT_A_MODERATOR_OR_AN_ADMIN("Server: you are not a moderator or an admin!"),
    YOU_HAVE_BEEN_KICKED_OUT("Server: you have been kicked out of the server!"),
    USER_IS_A_NEW_MODERATOR("Server: %s is the new moderator!"),
    USER_IS_ALREADY_A_MODERATOR("Server: this user is already a moderator!"),
    USER_IS_NOT_AN_ADMIN("Server: you are not an admin!"),
    USER_IS_MODERATOR_NOW("Server: you are the new moderator now!"),
    USER_IS_NO_LONGER_A_MODERATOR("Server: %s is no longer a moderator!"),
    USER_IS_NOT_A_MODERATOR("Server: this user is not a moderator!"),
    USER_IS_NOT_A_MODERATOR_ANY_MORE("Server: you are no longer a moderator!"),
    SERVER_NO_ONE_UNREAD("Server: no one unread"),
    SERVER_UNREAD_FROM("Server: unread from: %s"),
    IS_NOT_A_NUMBER("Server: %s is not a number!");

    private final String message;

    Responses(final String message) {
        this.message = message;
    }

    public void send(DataOutputStream outputStream) throws IOException {
        outputStream.writeUTF(this.message);
    }

    public void sendWithValues(DataOutputStream outputStream, String values) throws IOException {
        outputStream.writeUTF(String.format(this.message, values));
    }

    public void send(DataOutputStream outputStream, String message) throws IOException {
        outputStream.writeUTF(this.message.concat(message));
    }
}
