package server;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataBaseManager implements Serializable {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

        "server" + File.separator +
        "data";
    private static final String fileName = dbFilePath + File.separator + "db.json";

    DataBaseManager () throws IOException {
        try {
            if (!Files.exists(Paths.get(dbFilePath))) {
                Files.createDirectories(Paths.get(dbFilePath));
            }
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
            writer.write("{}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(JsonNode command) throws NullPointerException, JsonProcessingException {
        JsonNode container = null;
        try {
            rwl.readLock().lock();
            Reader reader = new BufferedReader(new FileReader(fileName));
            JsonParser jsonParser = mapper.getFactory().createParser(reader);
            container = jsonParser.readValueAsTree();
            rwl.readLock().unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonNode result = container;

        if (command.get("key").isArray()) {
            ArrayNode keysToSet = (ArrayNode) command.get("key");
            for (int i = 0; i < keysToSet.size(); i++) {
                result = result.get(keysToSet.get(i).asText());
            }
        } else {
            result = result.get(command.get("key").asText());
        }

        if (result != null) {
            return result.asText().isEmpty() ? result.toString() : result.toString();
        } else {
            throw new NullPointerException("Such element does not exists in DB");
        }
    }

    public synchronized void set(JsonNode command) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            rwl.readLock().lock();
            JsonParser jsonParser = mapper.getFactory().createParser(reader);
            JsonNode container = jsonParser.readValueAsTree();
            rwl.readLock().unlock();

            JsonNode pointer = container;
            String keyName = null;
            if (command.get("key").isArray()) {
                ArrayNode keysToSet = (ArrayNode) command.get("key");
                for (int i = 0; i < keysToSet.size() - 1; i++) {
                    keyName = keysToSet.get(i).asText();
                    pointer = pointer.get(keyName);
                }
                keyName = keysToSet.get(keysToSet.size() - 1).asText();
            } else {
                keyName = command.get("key").asText();
            }

            ((ObjectNode) pointer).set(keyName, command.get("value"));

            rwl.writeLock().lock();
            Writer writer = new BufferedWriter(new FileWriter(fileName, false));
            writer.write(container.toString());
            writer.close();
            rwl.writeLock().unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void delete(JsonNode command) throws JsonProcessingException {
        JsonNode container = null;
        try {
            rwl.readLock().lock();
            Reader reader = new BufferedReader(new FileReader(fileName));
            JsonParser jsonParser = mapper.getFactory().createParser(reader);
            container = jsonParser.readValueAsTree();
            reader.close();
            rwl.readLock().unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonNode pointer = container;
        String keyName = null;
        if (command.get("key").isArray()) {
            ArrayNode keysToSet = (ArrayNode) command.get("key");
            for (int i = 0; i < keysToSet.size() - 1; i++) {
                keyName = keysToSet.get(i).asText();
                pointer = pointer.get(keyName);
            }
            keyName = keysToSet.get(keysToSet.size() - 1).asText();
        } else {
            keyName = command.get("key").asText();
        }

        if (pointer.get(keyName) != null) {
            ((ObjectNode) pointer).remove(keyName);
        } else {
            throw new NullPointerException("Such element does not exists in DB");
        }

        System.out.println(container.toString());

        try {
            rwl.writeLock().lock();
            Writer writer = new BufferedWriter(new FileWriter(fileName, false));
            writer.write(container.toString());
            writer.close();
            rwl.writeLock().unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
