import java.util.*;

class FixingNullPointerException {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        string = "null".equals(string) ? null : string;
        try {
            System.out.println(string.toLowerCase());
        } catch (NullPointerException e) {
            System.out.println("NPE!");
        }
    }
}