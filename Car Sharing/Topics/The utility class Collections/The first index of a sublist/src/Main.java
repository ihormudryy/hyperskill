import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final List<String> list1 = Arrays.stream(scanner.nextLine().split("\\s+"))
                .collect(Collectors.toList());
        final List<String> list2 = Arrays.stream(scanner.nextLine().split("\\s+"))
                .collect(Collectors.toList());
        System.out.println(Collections.indexOfSubList(list1, list2) + " " + Collections.lastIndexOfSubList(list1, list2));
    }
}