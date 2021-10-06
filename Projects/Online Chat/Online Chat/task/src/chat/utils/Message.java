package chat.utils;

import java.io.Serializable;

public class Message implements Serializable {

    private Status status;
    private final String content;

    public Message(String content, boolean read) {
        this.status = read ? Status.READ : Status.NEW;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public boolean isNew() {
        return status == Status.NEW;
    }

    public void setRead() {
        this.status = Status.READ;
    }

    enum Status {
        NEW, READ
    }
}
