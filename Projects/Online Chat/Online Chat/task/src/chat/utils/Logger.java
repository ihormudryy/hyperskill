package chat.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public static void info(String message) {
        String now = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
                .format(LocalDateTime.now());
        System.out.println(now + ": [INFO] " + message);
    }
}
