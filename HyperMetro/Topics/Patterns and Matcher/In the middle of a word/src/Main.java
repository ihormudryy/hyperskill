import java.util.*;
import java.util.regex.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String part = scanner.nextLine();
        String line = scanner.nextLine();

        Pattern pattern = Pattern.compile("\\S" + part + "\\S", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);

        System.out.println(matcher.find() ? "YES" : "NO"); // false
    }
}