import java.lang.reflect.Modifier;
import java.util.Arrays;

class ReflectionUtils {

    public static int countPublicMethods(Class targetClass) {

        return (int) Arrays.stream(targetClass.getDeclaredMethods())
                           .map(method -> method.getModifiers())
                           .filter(modifiers -> Modifier.isPublic(modifiers))
                           .count();
    }
}