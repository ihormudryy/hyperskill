import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;

class Main {
    public static void main(String[] args) {
        System.out.println(Arrays.stream(new Scanner(System.in)
                .nextLine().split("\\s+"))
                .map(e -> Level.parse(e.toUpperCase()).intValue())
                .reduce(0, (s, e) -> s + e));
    }
}