import java.util.*;

public class Main {
    public static void main(String[] args) {
        String line = new Scanner(System.in).nextLine();
        System.out.println(line.matches(".*[A-Z].*") &&
                           line.matches(".*[a-z].*") &&
                           line.matches(".*[0-9].*") &&
                           line.matches(".{12,}") ? "YES" : "NO");
    }
}