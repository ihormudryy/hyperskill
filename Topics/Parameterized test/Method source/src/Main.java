import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class TestHelper {
    public static int[] primeGenerator() {
        List<Integer> result = new ArrayList<>();
        for (int i = 999; i > 10; i -= 2) {
            int k = i - 2;
            boolean isPrime = true;
            while (k > 1) {
                if (i % k == 0) {
                    isPrime = false;
                    break;
                }
                k -= 2;
            }
            if (isPrime) {
                result.add(i);
            }
        }
        int[] targetArray = result.stream().mapToInt(i -> i).toArray();
        return targetArray;
    }

//    public static void main(String[] args) {
//        Arrays.stream(primeGenerator()).forEach(e-> System.out.print(e + " "));
//    }
}