class Info {

    public static void printCurrentThreadInfo() {

        System.out.format("name: %s\npriority: %s",
                          Thread.currentThread().getName(),
                          Thread.currentThread().getPriority());
    }
}