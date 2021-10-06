import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int l = Integer.parseInt(scanner.nextLine());
        LocalTime deadline = LocalTime.parse("20:00");
        for (int i = 0; i < l; i++) {
            String[] s = scanner.nextLine().split("\\s+");
            if (deadline.isBefore(LocalTime.parse(s[1]))) {
                System.out.println(s[0]);
            }
        }
    }
}