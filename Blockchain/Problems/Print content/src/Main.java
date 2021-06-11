// do not remove imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

class ArrayUtils {
    public static <E> String info(E[] array) {
        return Arrays.stream(array)
                     .map(e -> String.valueOf(e))
                     .collect(Collectors.joining(", ","[","]"));
    }
}