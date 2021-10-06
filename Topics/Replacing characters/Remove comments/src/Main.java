import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String codeWithComments = scanner.nextLine();
        Pattern pattern1 = Pattern.compile("(\\/\\*.*?\\*\\/)");
        Pattern pattern2 = Pattern.compile("(\\/\\/.*?\\n+)");
        // System.out.println(pattern1.matcher(codeWithComments).replaceAll("").replaceAll());
        System.out.println(codeWithComments
                               .replaceAll("(\\/\\*.*?\\*\\/)", "")
                               .replaceAll("(\\/\\/.*+\\n?)", ""));
    }
}