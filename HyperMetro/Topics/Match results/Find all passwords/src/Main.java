import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static void main(String[] args) {
        String text = new Scanner(System.in).nextLine();
        Pattern pattern = Pattern.compile("password[:]*\\s*[a-zA-Z0-9]*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        boolean matched = false;
        while (matcher.find()) {
            String[] result = matcher.group().split("[:]+|\\s+");
            System.out.println(result[result.length > 2 ? 2 : 1].trim());
            matched = true;
        }
        System.out.print(matched ? "" : "No passwords found.");
    }
}