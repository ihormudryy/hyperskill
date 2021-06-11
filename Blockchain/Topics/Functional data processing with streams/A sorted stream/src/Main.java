import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    /**
     * Prints sorted elements of a given stream of strings.
     *
     * @param wordStream the input stream of strings
     */
    public static void sortAndPrint(Stream<String> wordStream) {
        wordStream.sorted().forEach(System.out::println);
    }

    // Don't change the code below
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        sortAndPrint(Arrays.stream(scanner.nextLine().split("\\s+")));
    }
}