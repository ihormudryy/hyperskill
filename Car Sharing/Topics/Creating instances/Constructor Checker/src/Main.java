import java.util.Arrays;

/**
 Check whether the class declares public parameterless constructor
 */
class ConstructorChecker {

    public static boolean checkPublicParameterlessConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getConstructors())
                .filter(c -> c.canAccess(null) && c.getParameterCount() == 0)
                .count() == 0 ? false : true;
    }
}