import java.util.Scanner;

public class Main {

    public static int convert(Long val) {
        if (val == null) {
            return 0;
        } else if (val.compareTo((long)Integer.MAX_VALUE) == 1) {
            return Integer.MAX_VALUE;
        } else if (val.compareTo((long)Integer.MIN_VALUE) == -1) {
            return Integer.MIN_VALUE;
        } else {
            return val.intValue();
        }
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String val = scanner.nextLine();
        Long longVal = "null".equals(val) ? null : Long.parseLong(val);
        System.out.println(convert(longVal));
    }
}