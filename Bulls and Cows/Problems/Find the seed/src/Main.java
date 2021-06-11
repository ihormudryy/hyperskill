import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int from = scanner.nextInt();
        int to = scanner.nextInt();
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        int minN = k;
        int seed = 0;
        for (int i = from; i <= to; i++) {
            int maxN = 0;
            Random random = new Random(i);
            for (int j = 0; j < n; j++) {
                int gen = random.nextInt(k);
                maxN = Math.max(maxN, gen);
            }
            if (maxN < minN) {
                minN = maxN;
                seed = i;
            }
        }
        System.out.format("%s\n%s\n", seed, minN);
    }
}