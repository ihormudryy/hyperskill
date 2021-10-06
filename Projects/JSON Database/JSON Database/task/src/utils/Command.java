package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;

public class Command {
    public Object type;
    public Object key;
    public Object value;

    public Command(Object type, Object key, Object value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }
}
