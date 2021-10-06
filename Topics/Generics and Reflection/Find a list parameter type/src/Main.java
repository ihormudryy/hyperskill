// Do not remove imports
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

class ListParameterInspector {
//    static class TestClass {
//        public List<String> listField;
//    }

    // Do not change the method
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String fieldName = scanner.next();

        ListParameterInspector inspector = new ListParameterInspector();
        inspector.printParameterType(new TestClass(), fieldName);
    }

    public void printParameterType(TestClass obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        //Type rawType = parameterizedType.getRawType();
        System.out.println(parameterizedType.getActualTypeArguments()[0].getTypeName());
    }
}