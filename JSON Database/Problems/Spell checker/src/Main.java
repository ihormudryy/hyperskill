import java.util.*;

class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Set<String> knownWords = new HashSet<>();
        int numberOfKnownWords = s.nextInt();
        for (int i = 0; i < numberOfKnownWords; i++) {
            knownWords.add(s.next().toLowerCase());
        }
        s.nextInt();
        Set<String> text = new TreeSet<>();
        while (s.hasNext()) {
            text.add(s.next().toLowerCase());
        }
        text.removeAll(knownWords);
        text.forEach(System.out::println);
    }
}