import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String stringWithHtmlTags = scanner.nextLine();
        // Pattern pattern = Pattern.compile("<\\w*/?\\w*>"); // a regex to match a digit
        Pattern pattern = Pattern.compile("\\<.*?\\>"); // a regex to match a digit
        System.out.println(pattern.matcher(stringWithHtmlTags).replaceAll(""));
    }
}