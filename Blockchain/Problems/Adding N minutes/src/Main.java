import java.time.LocalDateTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime time = LocalDateTime.parse(scanner.nextLine()).plusMinutes(scanner.nextInt());
        System.out.format("%s %s %s", time.getYear(),
                          time.getDayOfYear(),
                          time.toLocalTime());
    }
}