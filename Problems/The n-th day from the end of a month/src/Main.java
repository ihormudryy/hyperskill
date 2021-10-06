import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        LocalDate date = LocalDate.of(s.nextInt(), s.nextInt(), 1);
        System.out.format("%s",
                          date.plusDays(date.lengthOfMonth() - s.nextInt())
                              .format(DateTimeFormatter.ISO_DATE));
    }
}