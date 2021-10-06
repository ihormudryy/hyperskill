import java.util.Arrays;

class ThreadUtil {
    static void printNameOfTerminatedThread(Thread[] threads) {
        Arrays.stream(threads).filter(s -> s.getState() == Thread.State.TERMINATED)
                .forEach(s -> System.out.println(s.getName()));
    }
}