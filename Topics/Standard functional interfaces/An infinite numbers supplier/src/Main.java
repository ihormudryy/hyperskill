import java.util.function.*;

class FunctionUtils {

    public static Supplier<Integer> getInfiniteRange() {
        return new Supplier<Integer>() {
            public int index = 0;

            @Override
            public Integer get() {
                return index++;
            }
        };
    }
}