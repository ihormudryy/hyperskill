import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Arrays.stream(new Scanner(System.in).nextLine().split("\\s+"))
              .map(m -> m.replaceAll("[^a-zA-Z0-9]", ""))
              .map(String::toLowerCase)
              .sorted(Comparator.naturalOrder())
              .collect(Collectors.groupingBy(Function.identity(),
                                             LinkedHashMap::new,
                                             Collectors.counting()))
              .entrySet()
              .stream()
              .sorted(Comparator.comparing(Map.Entry::getValue,
                                           Comparator.reverseOrder()))
              .limit(10)
              .forEach(s -> System.out.println(s.getKey()));
    }
}