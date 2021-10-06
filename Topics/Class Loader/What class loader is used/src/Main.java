import java.util.Scanner;
class A {
}
class Main {
    public static void main(String... args) {
        System.out.println(Main.class.getClassLoader().getName());
        ClassLoader classLoader = Scanner.class.getClassLoader();
        System.out.println(classLoader);
        ClassLoader classLoader2 = A.class.getClassLoader();
        System.out.println(classLoader2.getName());
    }
}