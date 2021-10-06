import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class AsciiCharSequence implements java.lang.CharSequence{

    private byte[] sequence;

    public AsciiCharSequence(byte[] byteArray) {
        sequence = byteArray;
    }

    @Override
    public int length() {
        return sequence.length;
    }

    @Override
    public char charAt(int i) {
        return (char) sequence[i];
    }

    @Override
    public AsciiCharSequence subSequence(int from, int to) {
        return new AsciiCharSequence(Arrays.copyOfRange(sequence, from, to));
    }

    public String toString() {
        return new String(sequence);
    }
}