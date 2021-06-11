import java.math.BigDecimal;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BigDecimal x = new BigDecimal(scanner.nextLine());
        BigDecimal y = new BigDecimal(scanner.nextLine());
        System.out.println(x.multiply(y));
    }
}