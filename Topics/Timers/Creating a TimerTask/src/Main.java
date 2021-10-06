import java.util.Timer;
import java.util.TimerTask;

class TimerCode extends TimerTask {
    public void run() {
        System.out.println("Hello World!");
        System.exit(0);
    }
}

public class Main {
    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask task = new TimerCode();

        timer.schedule(task, 0L);
    }
}