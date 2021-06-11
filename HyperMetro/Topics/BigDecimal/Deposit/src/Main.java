import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BigDecimal x = new BigDecimal(scanner.nextLine());
        BigDecimal y = new BigDecimal(scanner.nextLine())
            .divide(BigDecimal.valueOf(100))
            .add(BigDecimal.valueOf(1))
            .pow(scanner.nextInt())
            .multiply(x);
        System.out.println("Amount of money in the account: " + y.setScale(2, RoundingMode.CEILING));
    }
}