// Do not remove imports
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Scanner;

class TypeVariablesInspector {

    public static void printTypeVariablesCount(TestClass obj, String methodName) throws Exception {
        System.out.println(obj.getClass().getDeclaredMethod(methodName).getTypeParameters().length);
    }
}