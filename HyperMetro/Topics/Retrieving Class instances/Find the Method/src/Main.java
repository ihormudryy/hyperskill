import java.util.Arrays;
import java.util.Objects;

class MethodFinder {

    public static String findMethod(String methodName, String[] classNames) {
        return Arrays.stream(classNames)
                     .map(clazz -> {
                         try {
                             return Class.forName(clazz);
                         } catch (ClassNotFoundException e) {
                         }
                         return null;
                     })
                     .filter(clazz -> Arrays.stream(clazz.getMethods())
                                            .filter(m -> m.getName().equals(methodName)).count() > 0)
                     .findAny()
                     .get()
                     .getName();
    }
}