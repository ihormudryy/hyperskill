package chat.server;

import chat.utils.ArgumentsWrapper;

import java.util.Optional;

@FunctionalInterface
public interface Command {

    Optional<String> execute(ArgumentsWrapper arguments);
}
