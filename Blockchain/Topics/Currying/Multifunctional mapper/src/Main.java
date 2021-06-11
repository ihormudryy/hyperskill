import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

class MultifunctionalMapper {
    /**
     * The function accepts a list of mappers and returns an operator that accepts a list of integers
     * and sequentially applies each mapper to each value (perform a transformation)
     */
    public static final Function<List<IntUnaryOperator>, UnaryOperator<List<Integer>>> MULTIFUNCTIONAL_MAPPER =
        a -> b -> b.stream()
                   .map(f -> a.stream()
                              .reduce(IntUnaryOperator::andThen)
                              .orElse(x -> x)
                              .applyAsInt(f))
                   .collect(Collectors.toList());
    /**
     * EXAMPLE: the operator transforms each number to the same number (perform the identity transformation)
     * <p>
     * It returns a list of the same numbers.
     */
    public static final UnaryOperator<List<Integer>> IDENTITY_TRANSFORMATION =
        MULTIFUNCTIONAL_MAPPER.apply(Arrays.asList(x -> x, x -> x, x -> x));
    /**
     * The operator accepts an integer list.
     * It multiplies by two each integer number and then add one to it.
     * <p>
     * The operator returns transformed integer list.
     */
    public static final UnaryOperator<List<Integer>> MULT_TWO_AND_THEN_ADD_ONE_TRANSFORMATION =
        a -> a.stream().map(x -> x * 2 + 1).collect(Collectors.toList());
    /**
     * The operator accepts an integer list.
     * It squares each integer number and then get the next even number following it.
     * <p>
     * The operator returns transformed integer list.
     */
    public static final UnaryOperator<List<Integer>> SQUARE_AND_THEN_GET_NEXT_EVEN_NUMBER_TRANSFORMATION =
        a -> a.stream().map(x -> x * x % 2 == 0 ? x * x + 2 : x * x + 1).collect(Collectors.toList());
    private static final String TAB = "    ";

    // Don't change the code below
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String[] values = scanner.nextLine().split(" ");

        List<Integer> numbers = Arrays.stream(values)
                                      .mapToInt(Integer::parseInt)
                                      .boxed()
                                      .collect(Collectors.toList());

        List<Integer> idMapper = MULTIFUNCTIONAL_MAPPER
            .apply(Arrays.asList(x -> x, x -> x, x -> x))
            .apply(numbers);

        List<Integer> idTransfomarmation =
            IDENTITY_TRANSFORMATION.apply(numbers);

        List<Integer> mult2AndAdd1Mapper = MULTIFUNCTIONAL_MAPPER
            .apply(Arrays.asList(x -> x * 2, x -> x + 1))
            .apply(numbers);

        List<Integer> mult2AndAdd1Tranformation =
            MULT_TWO_AND_THEN_ADD_ONE_TRANSFORMATION.apply(numbers);

        List<Integer> squareAndNextEvenMapper = MULTIFUNCTIONAL_MAPPER
            .apply(Arrays.asList(x -> x * x, x -> x % 2 == 0 ? x + 2 : x + 1))
            .apply(numbers);

        List<Integer> squareAndNextEvenNumberTransformation =
            SQUARE_AND_THEN_GET_NEXT_EVEN_NUMBER_TRANSFORMATION.apply(numbers);

        StringBuilder result = new StringBuilder("")
            .append(getStringFromList(idMapper))
            .append(TAB)
            .append(getStringFromList(idTransfomarmation))
            .append(TAB)
            .append(getStringFromList(mult2AndAdd1Mapper))
            .append(TAB)
            .append(getStringFromList(mult2AndAdd1Tranformation))
            .append(TAB)
            .append(getStringFromList(squareAndNextEvenMapper))
            .append(TAB)
            .append(getStringFromList(squareAndNextEvenNumberTransformation))
            .append(TAB);

        System.out.println(result.toString().trim());
    }

    private static String getStringFromList(List<Integer> numbers) {
        StringBuilder builder = new StringBuilder("");
        for (int n : numbers) {
            builder.append(n).append(" ");
        }
        return builder.toString().trim();
    }
}