import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.next();
            try {
                if (!s.equals("0")) {
                    System.out.println(Integer.parseInt(s) * 10);
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.format("Invalid user input: %s\n", s);
            }
        }
    }
}