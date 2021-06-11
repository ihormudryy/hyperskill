import java.util.Scanner;
import java.util.function.Function;

class CurryConcat {

    public static String calc(String str1, String str2, String str3, String str4) {

        Function<String, Function<String, Function<String, Function<String, String>>>> stringFun =
            a -> b -> c -> d -> a.toLowerCase()
                                 .concat(c.toUpperCase()).concat(b.toLowerCase()).concat(d.toUpperCase());
        return stringFun.apply(str1).apply(str2).apply(str3).apply(str4);
    }

    // Don't change the code below
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String[] values = scanner.nextLine().split(" ");
        System.out.println(calc(values[0], values[1], values[2], values[3]));
    }
}