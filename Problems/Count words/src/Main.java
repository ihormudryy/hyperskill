import java.io.BufferedReader;
import java.io.InputStreamReader;

class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int number = reader.readLine()
                           .trim()
                           .replaceAll("(.)\\1+", "$1")
                           .split(" ").length;
        System.out.println(number == 1 ? 0 : number);
        reader.close();
    }
}