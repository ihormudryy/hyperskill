import java.math.BigInteger;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        BigInteger m = new BigInteger(scanner.next());
        BigInteger result = BigInteger.ONE;
        int index = 1;
        while (result.compareTo(m) < 0) {
            result = result.multiply(BigInteger.valueOf(index++));
        }
        System.out.print(--index);
    }
}