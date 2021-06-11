import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println(LocalTime.ofSecondOfDay(s.nextInt()));
    }
}