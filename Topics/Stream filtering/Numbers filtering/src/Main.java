import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class EvenAndOddFilter {

    public static IntStream createFilteringStream(IntStream evenStream, IntStream oddStream) {
        return IntStream.concat(evenStream, oddStream).sorted()
                        .filter(a -> a % 3 == 0).filter(a -> a % 5 == 0).skip(2);
    }

    // Don't change the code below
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String[] values = scanner.nextLine().split(" ");

        List<Integer> numbers = Arrays.stream(values)
                                      .map(Integer::parseInt)
                                      .collect(Collectors.toList());

        int[] evenNumbers = numbers.stream()
                                   .filter(n -> n % 2 == 0)
                                   .mapToInt(x -> x)
                                   .toArray();

        int[] oddNumbers = numbers.stream()
                                  .filter(n -> n % 2 == 1)
                                  .mapToInt(x -> x)
                                  .toArray();

        IntStream filteringStream = createFilteringStream(
            Arrays.stream(evenNumbers),
            Arrays.stream(oddNumbers));

        System.out.println(filteringStream.boxed().collect(Collectors.toList()));
    }
}