import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            LinkedList list = new LinkedList();
            int end = scanner.nextInt();
            for (int i = 0; i <= end; i++) {
                String[] s = scanner.nextLine().trim().split("\\s+");
                switch(s[0]) {
                    case "addLast":
                        list.addLast(s[1]);
                        break;
                    case "addFirst":
                        list.addFirst(s[1]);
                        break;
                    case "removeFirst":
                        list.removeFirst();
                        break;
                    case "removeLast":
                        list.removeLast();
                        break;
                    default:
                        Collections.reverse(list);
                }
            }
            list.forEach(e -> System.out.print(e + " "));
        }
    }
}