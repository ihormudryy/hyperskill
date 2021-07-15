import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 Get an array of fields the object declares that contain annotations (inherited fields should be skipped).
 */
class AnnotationsUtil {

    public static String[] getFieldsContainingAnnotations(Object object) {
        List<String> array = new ArrayList<>();
//        List<String> array = Arrays.stream(object.getClass()
//                                    .getDeclaredFields())
//                .peek(f -> System.out.println("s " + Arrays.stream(f.getAnnotations()).peek(a -> System.out.println("a "+ a))))
//                .filter(f -> f.getDeclaredAnnotations().length != 0)
//                .map(f -> f.getName())
//                .collect(Collectors.toList());
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getAnnotations().length > 0) {
                //System.out.println(field.getName());
                array.add(field.getName());
            }
        }
        return array.stream().toArray(String[]::new);
    }

//    public static void main(String[] args) {
//        Arrays.stream(getFieldsContainingAnnotations(new String())).peek(System.out::println);
//    }
}