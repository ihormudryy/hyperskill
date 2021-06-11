class CountDownCounter {

    volatile int count;

    public CountDownCounter(int initial) {
        this.count = initial;
    }

    public synchronized void decrement() {
        count--;
    }
}