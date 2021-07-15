import java.util.*;


class MapFunctions {

    public static void calcTheSamePairs(Map<String, String> map1, Map<String, String> map2) {
        System.out.println(map1.keySet().stream()
                .filter(key -> map2.containsKey(key) && map1.get(key)
                        .equals(map2.get(key))).count());

    }
}