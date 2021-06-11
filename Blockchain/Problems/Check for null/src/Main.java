// do not remove imports
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

class ArrayUtils {
    public static <E> boolean hasNull(E[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                return true;
            }
        }
        return false;
    }
}