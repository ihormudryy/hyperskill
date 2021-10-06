import java.util.Scanner;

class Util {
    public static void swapInts(Integer x, Integer y) {
        Integer tmp = Main.x;
        Main.x = Integer.valueOf(y);
        Main.y = Integer.valueOf(tmp);
    }
}

class Main {
    public static Integer x;
    public static Integer y;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            x = Integer.parseInt(scanner.nextLine());
            y = Integer.parseInt(scanner.nextLine());

            Util.swapInts(x, y); // fixme this method doesn't work as expected

            System.out.println(x);
            System.out.println(y);
        }
    }
}