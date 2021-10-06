import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime end = LocalDateTime.parse(scanner.nextLine());
        LocalDateTime start = LocalDateTime.of(end.getYear(), 1, 1, 0,0);
        System.out.println(ChronoUnit.HOURS.between(start, end));
    }
}