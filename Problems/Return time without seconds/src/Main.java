import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        LocalTime time = LocalTime.parse(s.nextLine(), DateTimeFormatter.ISO_LOCAL_TIME);
        System.out.format("%s:%s", time.getHour() == 0 ? "00" : time.getHour(), time.getMinute());
    }
}