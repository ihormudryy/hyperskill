import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Main {

    private static List<LongStream> invertedStreams(List<LongStream> streams) {
        return streams.stream().map(s -> s.isParallel() ? s.sequential() : s.parallel())
                      .collect(Collectors.toList());
    }

    /* Do not modify the code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Boolean> parallelFlags = Arrays
            .stream(scanner.nextLine().split("\\s+"))
            .map(Boolean::parseBoolean)
            .collect(Collectors.toList());

        // :)
        List<LongStream> streams = Stream
            .iterate(0,
                     i -> i < parallelFlags.size(),
                     i -> i + 1)
            .map(i -> {
                var stream = LongStream.of();
                if (parallelFlags.get(i)) {
                    stream = stream.parallel();
                }
                return stream;
            }).collect(Collectors.toList());

        List<String> invertedParallelFlagsAsStrings =
            invertedStreams(streams).stream()
                                    .map(LongStream::isParallel)
                                    .map(Object::toString)
                                    .collect(Collectors.toList());

        System.out.println(String.join(" ", invertedParallelFlagsAsStrings));
    }
}