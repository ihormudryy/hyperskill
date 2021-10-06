import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String stringWithNumbers = scanner.nextLine();
        Pattern javaPattern = Pattern.compile("\\d{10,}");
        Matcher matcher = javaPattern.matcher(stringWithNumbers);

        while (matcher.find()) {
            System.out.println(matcher.group() + ":" + matcher.group().length());
        }
    }
}