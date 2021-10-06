class Apple {

    private String appleVariety;

    public Apple(String appleVariety) {
        this.appleVariety = appleVariety;
    }

    void cutApple() {

        class Knife {
            public void makeSlices () {
                System.out.println("Apple " + Apple.this.appleVariety + " is ready to be eaten!");
            }
        }

        Knife knife = new Knife();
        knife.makeSlices();
    }

    public static void main(String[] args) {
        Apple apple = new Apple("Gala");
        apple.cutApple();
    }
}