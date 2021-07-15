class Person {
    // Do not change these fields
    public static final String DEFAULT_NAME = "Unknown";
    public static final int MAX_AGE = 130;
    public static final int MIN_AGE = 0;
    private String name;
    private int age;

    // Fix the constructor to make its code pass the unit tests
    Person(String name, int age) {
        this.name = name == null || name.isEmpty() || name.equals("\t") ? DEFAULT_NAME : name;
        if (age > this.MAX_AGE) {
            this.age = this.MAX_AGE;
        } else if (age < this.MIN_AGE) {
            this.age = this.MIN_AGE;
        } else {
            this.age = age;
        }
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}