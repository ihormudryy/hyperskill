import java.util.Arrays;
import java.util.Scanner;

class ArrayCalc {

    // static nested class
    public static class MinMaxPair {
        private int min;
        private int max;

        public MinMaxPair(int first, int second) {
            this.min = first;
            this.max = second;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }
    }

    // find min and max elements
    public static MinMaxPair findMinMax(int[] array) {

        return new MinMaxPair(Arrays.stream(array).min().getAsInt(), Arrays.stream(array).max().getAsInt());
    }
}

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // input array 
        int[] array = new int[10];
        for (int i = 0; i < 10; i++) {
            array[i] = scanner.nextInt();
        }

        ArrayCalc.MinMaxPair p = ArrayCalc.findMinMax(array);

        System.out.println("min = " + p.getMin());
        System.out.println("max = " + p.getMax());
    }
}