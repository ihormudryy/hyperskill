class SimpleCounter {
    private static SimpleCounter instance = new SimpleCounter();
    public int counter;

    private SimpleCounter () {}

    public static SimpleCounter getInstance() {
        return instance;
    }
}