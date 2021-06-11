import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        InputStream inputStream = System.in;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        int ch = reader.read();
        while (ch != -1) {
            System.out.print(ch);
            ch = reader.read();
        }
    }
}