class Starter {

    public static void startRunnables(Runnable[] runnables) {
        for (Runnable thread:runnables) {
            new Thread(thread).start();
        }
    }
}