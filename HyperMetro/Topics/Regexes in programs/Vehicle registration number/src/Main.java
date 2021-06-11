import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String regNum = scanner.nextLine(); // a valid or invalid registration number
        String list = "[ABEKMHOPCTYX]";
        String regex = String.format("%s?[0-9]{3}%s{2}", list, list);
        System.out.println(regNum.matches(regex));
    }
}