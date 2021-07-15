import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String plain = scanner.nextLine();
        String encoded = scanner.nextLine();
        String toDecode = scanner.nextLine();
        String toEncode = scanner.nextLine();
        Map<Character, Character> encodeTable = new HashMap<>();
        Map<Character, Character> decodeTable = new HashMap<>();

        for (int i = 0; i < plain.length(); i++) {
            encodeTable.put(plain.charAt(i), encoded.charAt(i));
            decodeTable.put(encoded.charAt(i), plain.charAt(i));
        }

        for (int i = 0; i < toDecode.length(); i++) {
            System.out.print(encodeTable.get(toDecode.charAt(i)));
        }
        System.out.println();
        for (int i = 0; i < toEncode.length(); i++) {
            System.out.print(decodeTable.get(toEncode.charAt(i)));
        }
    }
}