import java.util.Arrays;
import java.util.Comparator;

/* Please, do not rename it */
class Problem {

    public static void main(String[] args) {
s        switch (args[0]) {
            case "MAX":
                System.out.print(Arrays.stream(args, 1, args.length)
                        .max(Comparator.comparing(Integer::parseInt)).get());
                break;
            case "MIN":
                System.out.print(Arrays.stream(args, 1, args.length)
                        .min(Comparator.comparing(Integer::parseInt)).get());
                break;
            case "SUM":
                System.out.print(Arrays.stream(args, 1, args.length)
                        .mapToInt(Integer::parseInt)
                        .sum());
                break;
            default:
                System.out.print("Unknown operator");
        }
    }
}