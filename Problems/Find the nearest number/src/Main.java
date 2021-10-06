import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    private static int delta = Integer.MAX_VALUE;

    public static void compare(int i, int n) {
        delta = Math.abs(i - n) <= delta ? Math.abs(i - n) : delta;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String[] inputs = s.nextLine().split(" ");
        int num = s.nextInt();
        Arrays.stream(inputs).mapToInt(Integer::parseInt).forEach(n -> compare(n, num));
        Arrays.stream(inputs).mapToInt(Integer::parseInt)
              .filter(n -> n + delta == num || n - delta == num)
              .sorted()
              .forEach(n -> System.out.format("%s ", n));
    }
}