import java.util.function.LongUnaryOperator;

class Operator {

    public static LongUnaryOperator unaryOperator = x -> (x + 1) % 2 == 0 ? x + 1 : x + 2;
}