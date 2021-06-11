import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BadWordsDetector {

    private static Stream<String> createBadWordsDetectingStream(String text,
                                                                List<String> badWords) {
        return Arrays.stream(text.split("\\s+"))
                     .filter(word -> badWords.contains(word))
                     .sorted()
                     .distinct();
    }

    /* Do not change the code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] parts = scanner.nextLine().split(";");

        // the first part is a text
        String text = parts[0];

        // the second part is a bad words dictionary
        List<String> dict = parts.length > 1 ?
            Arrays.asList(parts[1].split(" ")) :
            Collections.singletonList("");

        System.out.println(createBadWordsDetectingStream(text, dict).collect(Collectors.toList()));
    }

}