import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {

    static int recursiveDepthCalculation(int i, ArrayList<Integer> parent, ArrayList<Integer> height) {
        if (parent.get(i) == -1) {
            return 1;
        }

        if (height.get(i) != null && height.get(i) != -1) {
            return height.get(i);
        }

        height.set(i, 1 + recursiveDepthCalculation(parent.get(i), parent, height));
        return height.get(i);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> nodesList = new ArrayList<>();
        ArrayList<Integer> height = new ArrayList<>();
        int res = 0;
        IntStream.range(0, scanner.nextInt()).forEach(i -> {
            nodesList.add(i, scanner.nextInt());
            height.add(i, -1);
        });
        for (int i = 0; i < nodesList.size(); i++) {
            res = Math.max(res, recursiveDepthCalculation(i, nodesList, height));
        }
        System.out.println(res);

        /**
         *       This algorithm below is based on queue and depth hash map
         */

        // HashMap<Integer, Integer> depthMap = new HashMap<>();
        // depthMap.put(-1, 1);
        // LinkedList<Integer> queue = new LinkedList<Integer>();
        // queue.add(nodesList.indexOf(-1));
        // while (!queue.isEmpty()) {
        //     int head = queue.pop();
        //     for (int i = nodesList.indexOf(head);
        //          i <= nodesList.lastIndexOf(head)
        //              && nodesList.indexOf(head) != -1;
        //          i++) {
        //         int prev = nodesList.get(head);
        //         int curr = nodesList.get(i);
        //         if (curr == head) {
        //             //System.out.println(i + " --> " + head + " --> " + prev);
        //             if (depthMap.get(curr) == null) {
        //                 depthMap.put(curr, depthMap.get(prev) + 1);
        //             }
        //             queue.push(i);
        //         }
        //     }
        // }
        // System.out.println(Collections.max(depthMap.entrySet(), Map.Entry.comparingByValue()).getValue());
        // });
    }
}