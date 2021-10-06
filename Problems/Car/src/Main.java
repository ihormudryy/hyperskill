class Car {

    int yearModel;
    String make;
    int speed;

    public void accelerate() {
        speed += 5;
    }

    public void brake() {
        speed = (speed - 5 > 0) ? speed - 5 : 0;
    }
}