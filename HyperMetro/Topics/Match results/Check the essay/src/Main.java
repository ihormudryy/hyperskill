import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class FindTheKeys {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        Pattern pattern = Pattern.compile("the(\\s+)key(\\s+)is(\\s+)([!#?aeiou]+|[\\w&&[^aeiou]]+)" +
                                              "(\\s+)",
                                          Pattern.CASE_INSENSITIVE);
        pattern.matcher(text)
               .results()
               .forEach(m -> {
                   String[] result = m.group().split("(?i)the(\\s+)key(\\s+)is(\\s+)");
                   System.out.println(result[1].trim());
               });
    }
}