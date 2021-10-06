package bullscows;

import java.util.Random;
import java.util.Scanner;

class Grader {

    private char[] secret;
    Random randomizer = new Random();

    private String maskSecret(int length) {
        String res = "";
        for (int i = 0; i < length; i++) {
            res += "*";
        }
        return res;
    }
    public void generateSecret(int length, int num) throws Exception {
        if (length == 0 || num > 36) {
            System.out.format("Error: maximum number of possible symbols in the code is 36 (0-9, a-z)\n");
            throw new Exception();
        } else if (num < length) {
            System.out.format("Error: it's not possible to generate a code with a length of %s with %s unique symbols.\n", length, num);
            throw new Exception();
        } else {
            secret = new char[length];
            int[] usageMap = new int[num];
            int i = 0;
            while (i < length) {
                int r = randomizer.nextInt(num);
                if (usageMap[r] != 1) {
                    secret[i] = (r < 10) ? (char)(r + 48) : (char)(r + 87);
                    usageMap[r] = 1;
                    i++;
                }
            }
            String range = (num <= 10) ? "0, " + (num - 1) : "0-" + ((num - 1) % 10) + ", a-" + (char)(num + 86);
            System.out.format("The secret is prepared. %s (%s).\n", maskSecret(length), range);
        }
    }

    public int calcBullsAndCows(String guess) {
        char[] g = guess.toCharArray();
        int bulls = 0;
        int cows = 0;
        for (int i = 0; i < secret.length; i++) {
            for (int j = 0; j < g.length; j++) {
                if (secret[i] == g[j] && i != j && secret[i] != g[i]) {
                    cows++;
                } else if (secret[i] == g[j] && i == j) {
                    bulls++;
                }
            }
        }
        System.out.format("Grade: %s bull(s) and %s cow(s)\n", bulls, cows, new String(secret));
        return bulls;
    }
}

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        Grader grader = new Grader();
        System.out.println("Input the length of the secret code:");
        String l = null;
        int len = 0;
        try {
            l = scanner.next();
            len = Integer.valueOf(l);
        } catch (Exception e) {
            System.out.format("Error: \"%s\" isn't a valid number.", l);
            return;
        }

        System.out.println("Input the number of possible symbols in the code:");
        int nums = scanner.nextInt();
        try {
            grader.generateSecret(len, nums);
        } catch (Exception e) {
            return;
        }

        System.out.println("Okay, let's start a game!\n");
        boolean guessed = false;
        long i = 1;
        while (!guessed) {
            System.out.format("Turn %s:\n", i++);
            String guess = scanner.next();
            if (grader.calcBullsAndCows(guess) == len) {
                System.out.println("Congratulations! You guessed the secret code.");
                guessed = true;
            }
        }
    }
}
