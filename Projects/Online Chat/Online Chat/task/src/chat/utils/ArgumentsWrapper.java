package chat.utils;

import java.io.DataOutputStream;
import java.util.Map;
import java.util.Optional;

public class ArgumentsWrapper {
    private DataOutputStream output;
    private String request;
    private Map<String, User> usersMainMap;
    private Optional<String> receiverUser = Optional.empty();
    private Optional<String> currentUser = Optional.empty();

    public ArgumentsWrapper() {
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    public ArgumentsWrapper setOutput(DataOutputStream output) {
        this.output = output;
        return this;
    }

    public Map<String, User> getUsersMainMap() {
        return usersMainMap;
    }

    public ArgumentsWrapper setUsersMainMap(Map<String, User> usersMainMap) {
        this.usersMainMap = usersMainMap;
        return this;
    }

    public Optional<String> getReceiverUser() {
        return receiverUser;
    }

    public ArgumentsWrapper setReceiverUser(Optional<String> receiverUser) {
        this.receiverUser = receiverUser;
        return this;
    }

    public Optional<String> getCurrentUser() {
        return currentUser;
    }

    public ArgumentsWrapper setCurrentUser(Optional<String> currentUser) {
        this.currentUser = currentUser;
        return this;
    }
}
