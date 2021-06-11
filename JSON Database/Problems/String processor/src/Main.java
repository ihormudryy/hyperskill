import java.util.Scanner;

class StringProcessor extends Thread {

    final Scanner scanner = new Scanner(System.in); // use it to read string from the standard input

    public void validateString() {
        String input = scanner.nextLine();
        while (scanner.hasNext()){
            if (input.matches("^[A-Z]+$")) {
                System.out.println("FINISHED");
                return;
            } else {
                System.out.println(input.toUpperCase());
            }
            input = scanner.nextLine();
        }
    }

    @Override
    public void run() {
        validateString();
    }
}