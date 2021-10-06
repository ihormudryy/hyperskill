import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int lines = Integer.parseInt(scanner.nextLine());
        Set<Integer> parents = new HashSet<>();
        Set<Integer> children = new HashSet<>();
        for (int i = 0; i < lines; i++) {
            int[] nodes = Arrays.stream(scanner.nextLine().split("\\s+"))
                                .mapToInt(Integer::parseInt).toArray();
            parents.add(nodes[0]);
            children.add(nodes[1]);
        }
        children.removeAll(parents);
        System.out.println(children.size());
        children.forEach(m -> System.out.print(m.intValue() + " "));
    }
}