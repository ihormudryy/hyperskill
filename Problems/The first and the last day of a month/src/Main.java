import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        LocalDate date = LocalDate.of(s.nextInt(), s.nextInt(), 1);
        System.out.format("%s %s", date.format(DateTimeFormatter.ISO_DATE),
                          date.plusDays(date.lengthOfMonth() - 1).format(DateTimeFormatter.ISO_DATE));
    }
}