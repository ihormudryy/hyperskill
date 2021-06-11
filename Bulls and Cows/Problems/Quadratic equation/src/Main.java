import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        double a = scanner.nextFloat();
        double b = scanner.nextFloat();
        double c = scanner.nextFloat();
        double d = Math.sqrt(Math.pow(b, 2) - 4 * a * c);
        double x1 = (-b - d) / (2 * a);
        double x2 = (-b + d) / (2 * a);

        System.out.format("%s %s", (float) Math.min(x1, x2), (float) Math.max(x1, x2));
    }
}