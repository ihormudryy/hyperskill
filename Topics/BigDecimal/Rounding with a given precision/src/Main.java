import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BigDecimal x = new BigDecimal(scanner.nextLine());
        int newScale = scanner.nextInt();
        System.out.println(x.setScale(newScale, RoundingMode.HALF_DOWN));
    }   
}