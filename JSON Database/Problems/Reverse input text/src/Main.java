import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(new StringBuilder(reader.readLine()).reverse());
        reader.close();
    }
}