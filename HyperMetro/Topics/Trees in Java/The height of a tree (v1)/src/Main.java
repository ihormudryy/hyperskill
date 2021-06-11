import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static int fetchRecursive(ArrayList<Integer> nodes, int index) {
        if (nodes.get(index).equals(-1)) {
            return 1;
        }
        return 1 + fetchRecursive(nodes, nodes.get(index));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        ArrayList<Integer> nodesList = Arrays.stream(scanner.nextLine().split("\\s+"))
                                             .mapToInt(Integer::parseInt)
                                             .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        int max = 0;
        for (int i = 0; i < nodesList.size(); i++) {
            if (nodesList.indexOf(i) == -1) {
                max = Math.max(max, fetchRecursive(nodesList, i));
            }
        }
        System.out.println(max);
    }
}