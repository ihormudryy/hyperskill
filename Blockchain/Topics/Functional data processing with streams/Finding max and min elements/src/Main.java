import java.util.*;
import java.util.function.*;
import java.util.stream.*;


class MinMax {

    public static <T> void findMinMax(
            Stream<? extends T> stream,
            Comparator<? super T> order,
            BiConsumer<? super T, ? super T> minMaxConsumer) {
        Map<String, T> map = new HashMap();
        map.put("mix", null);
        map.put("max", null);
        stream.forEachOrdered(e -> {
            if (map.get("min") == null || order.compare(e, map.get("min")) < 0) {
                map.put("min", e);
            }
            if (map.get("max") == null || order.compare(e, map.get("max")) > 0) {
                map.put("max", e);
            }
        });
        minMaxConsumer.accept(map.get("min"), map.get("max"));
    }
}