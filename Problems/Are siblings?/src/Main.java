import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("/home/wise/Downloads/dataset_91033.txt");
        Scanner s = new Scanner(file);
        s.nextLine();
        int year = s.nextInt();
        int population = s.nextInt();
        while (s.hasNext()) {
            System.out.println(s.nextLine());

        }
    }
}