import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


class CollectorPalindrome {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String[] words = scanner.nextLine().split(" ");

        Map<Boolean, List<String>> palindromeOrNo = Arrays.stream(words)
                .collect(
                    Collectors.partitioningBy(s -> s.equalsIgnoreCase(String.valueOf((new StringBuilder(s)).reverse())))
                );

        palindromeOrNo = new LinkedHashMap<>(palindromeOrNo);

        System.out.println(palindromeOrNo);
    }
}