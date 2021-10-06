// do not remove imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

class ArrayUtils {
    public static <E> E[] invert(E[] array) {
        Collections.reverse(Arrays.asList(array));
        return array;
    }
    HashTable<String> table = new HashTable(5);

}