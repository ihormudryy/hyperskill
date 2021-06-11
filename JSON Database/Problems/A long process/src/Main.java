import java.util.Scanner;

public class Main {

    private static String message;
    private static int errorCode;

    public static void main(String[] args) {

        final Scanner scanner = new Scanner(System.in);
        message = scanner.nextLine();
        errorCode = Integer.parseInt(scanner.nextLine());

        startLongProcess(message, errorCode, new Callback() {
            @Override
            public void onStarted() {
                System.out.println("The process started");
            }

            @Override
            public void onStopped(String cause) {
                System.out.println(cause);
            }

            @Override
            public void onFinished(int code) {
                if (code > 0) {
                    System.out.println("The process is finished with error: " + code);
                } else {
                    System.out.println("The process successfully finished");
                }
            }
        });

    }

    public static void startLongProcess(String message, int errorCode,Callback callback) {
        callback.onStarted();
        callback.onStopped(message);
        callback.onStarted();
        callback.onFinished(errorCode);
    }

}

interface Callback {

    void onStarted();

    void onStopped(String cause);

    void onFinished(int code);
}