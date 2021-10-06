import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

class Main {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int range = in.nextInt();
    Deque<Integer> q1 = new ArrayDeque();
    Deque<Integer> q2 = new ArrayDeque();
    int sumQ1 = 0;
    int sumQ2 = 0;
    for (int i = 0; i < range; i++) {
      if (sumQ2 < sumQ1) {
        q2.add(in.nextInt());
        sumQ2 += in.nextInt();
      } else {
        q1.add(in.nextInt());
        sumQ1 += in.nextInt();
      }
    }
      q1.forEach(e -> System.out.print(e + " "));
    System.out.println();
    q2.forEach(e -> System.out.print(e + " "));
  }
}