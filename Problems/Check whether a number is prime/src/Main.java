import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        while (scanner.hasNext()) {
            int number = scanner.nextInt();
            executor.submit(() -> {
                try {
                    PrintIfPrimeTask task = new PrintIfPrimeTask(number);
                    task.start();
                } catch (Exception e) {
                    System.out.println("Time limit exceed");
                }
            });
        }
        executor.shutdown();
    }
}

class PrintIfPrimeTask extends Thread {
    private final int number;

    public PrintIfPrimeTask(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        int index = number - 1;
        boolean isPrime = true;
        while (index > 1) {
            if (number % index-- == 0) {
                isPrime = false;
                break;
            }
        }
        System.out.print(isPrime && number != 1 ? number + "\n" : "");
    }
}