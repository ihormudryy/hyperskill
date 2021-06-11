class MessageNotifier extends Thread {
    String message;
    int repeat;

    public MessageNotifier(String msg, int repeats) {
        message = msg;
        repeat = repeats;
    }

    @Override
    public void run() {
        for (int i = 0; i < repeat; i++) {
            System.out.println(message);
        }
    }
}