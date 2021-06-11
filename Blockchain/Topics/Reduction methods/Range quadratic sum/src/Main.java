import java.util.stream.LongStream;

class QuadraticSum {
    public static long rangeQuadraticSum(int fromIncl, int toExcl) {

        return LongStream.range(fromIncl, toExcl)
                         .reduce(0L, (x, y) -> x + y * y);
    }
}