class ComputerFacadeTestDrive {
    public static void main(String[] args) {
        /* Your subsystems */

        ComputerFacade computerFacade = new ComputerFacade();

        computerFacade.turnOnComputer();
        computerFacade.turnOffComputer();
    }
}

class ComputerFacade {
    Processor processor;
    Monitor monitor;
    Keyboard keyboard;

    public ComputerFacade() {
        processor = new Processor();
        monitor = new Monitor();
        keyboard = new Keyboard();
    }

    public void turnOnComputer() {
        processor.on();
        monitor.on();
        keyboard.on();
    }

    public void turnOffComputer() {
        keyboard.off();
        monitor.off();
        processor.off();
    }
}

class Processor {
    /* Your subsystem description */

    public void on() {
        System.out.println("Processor on");
    }

    public void off() {
        System.out.println("Processor off");
    }
}

class Monitor {
    /* Your subsystem description */

    public void on() {
        System.out.println("Monitor on");
    }

    public void off() {
        System.out.println("Monitor off");
    }
}

class Keyboard {
    /* Your subsystem description */

    public void on() {
        System.out.println("Keyboard on");
        turnOnBacklight();
    }

    public void off() {
        System.out.println("Keyboard off");
        turnOffBacklight();
    }

    private void turnOnBacklight() {
        System.out.println("Backlight is turned on");
    }

    private void turnOffBacklight() {
        System.out.println("Backlight is turned off");
    }
}