class ClassCountingInstances {

    private static long numberOfInstances;

    public ClassCountingInstances() {
        synchronized (ClassCountingInstances.class) {
            numberOfInstances++;
        }
    }

    public static synchronized long getNumberOfInstances() {
        return numberOfInstances;
    }
}