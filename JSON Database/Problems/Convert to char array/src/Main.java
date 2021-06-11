import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.CharBuffer;

class Converter {
    public static char[] convert(String[] words) throws IOException {
        CharArrayWriter writer = new CharArrayWriter();
        for (String w: words) {
            writer.write(w);
        }
        return writer.toCharArray();
    }
}