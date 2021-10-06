import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = Integer.parseInt(scanner.nextLine());
        String line = scanner.nextLine();
        String p = "\\b[a-zA-Z]{" + size + "}\\b";
        Pattern pattern = Pattern.compile(p);
        System.out.println(pattern.matcher(line).find() ? "YES" : "NO"); // false
    }
}