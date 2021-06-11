import java.util.Comparator;
import java.util.List;

class Utils {

    public static void sortStrings(List<String> strings) {
        strings.sort(Comparator.reverseOrder());
    }
}
