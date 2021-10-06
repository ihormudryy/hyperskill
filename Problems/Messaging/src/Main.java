import java.util.concurrent.*;

/* Do not change this class */
class Message {
    final String text;
    final String from;
    final String to;

    Message(String from, String to, String text) {
        this.text = text;
        this.from = from;
        this.to = to;
    }
}

/* Do not change this interface */
interface AsyncMessageSender {
    void sendMessages(Message[] messages);
    void stop();
}

class AsyncMessageSenderImpl implements AsyncMessageSender {
    ThreadFactory threadFactory;

    @Override
    public void stop() {
        executor.shutdown();
    }

    private ExecutorService executor = Executors.newFixedThreadPool(6);
    private final int repeatFactor;

    public AsyncMessageSenderImpl(int repeatFactor) {
        this.repeatFactor = repeatFactor;
    }

    @Override
    public void sendMessages(Message[] messages) {
        for (Message msg : messages) {
            for (long i = 0; i < repeatFactor; i++) {
                executor.submit(() -> {
                    System.out.printf("(%s>%s): %s\n", msg.from, msg.to, msg.text);
                });
            }
        }
    }
}
/*
class Main {
    public static void main(String[] argv) throws InterruptedException {
        AsyncMessageSender sender = new AsyncMessageSenderImpl(3);

        Message[] messages = {
            new Message("John", "Mary", "Hello!"),
            new Message("Clara", "Bruce", "How are you today?")
        };

        sender.sendMessages(messages);

        sender.stop();
    }
}*/