// You can experiment here, it won’t be checked

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Task {
    public static void main(String[] args) {
        List<String> countries = Arrays.asList("Costa Rica", "Greece", "Malaysia", "Peru");
        List<Integer> numbers = countries.stream()
                                         .map(country -> country.split("\\s+"))
                                         .flatMap(Arrays::stream)
                                         .map(String::length)
                                         .collect(Collectors.toList());
        numbers.forEach(n -> System.out.println(n));
    }
}
