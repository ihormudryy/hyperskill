import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class AnnotationUtils {

    public static void printClassAnnotations(Class classObject) {
        for (Annotation annotation : classObject.getAnnotations()) {
            System.out.println(annotation.annotationType().getSimpleName().replace("@", ""));
        }
    }
}