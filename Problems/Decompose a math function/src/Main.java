import java.util.Scanner;

class MultipleFunction {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double x = scanner.nextDouble();
        System.out.println(f(x));
    }

    public static double f(double x) {
        if (x <= 0) {
            return f1(x);
        } else if (x > 0 && x < 1) {
            return f2(x);
        } else {
            return f3(x);
        }
    }

    //implement your methods here
    public static double f1(double d) {
        return d * d + 1;
    }

    public static double f2(double d) {
        return 1 / (d * d);
    }

    public static double f3(double d) {
        return d * d - 1;
    }
}