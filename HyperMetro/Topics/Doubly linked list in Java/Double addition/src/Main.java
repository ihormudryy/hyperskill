import java.util.LinkedList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int end = s.nextInt();
        LinkedList linkedList = new LinkedList();
        for (int i = 0; i < end; i++) {
            int v = s.nextInt();
            if (i < 2 || Math.abs((int) linkedList.getFirst() - v) >= Math.abs((int) linkedList.getLast() - v)) {
                linkedList.addLast(v);
            } else {
                linkedList.addFirst(v);
            }
        }
        linkedList.forEach(e -> System.out.print(e + " "));
    }
}