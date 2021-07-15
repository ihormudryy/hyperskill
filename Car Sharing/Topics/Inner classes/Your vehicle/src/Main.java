class EnjoyVehicle {

    public static void startVehicle() {
        Vehicle v = new Vehicle();
        Vehicle.Engine engine = v.new Engine();
        engine.start();
    }
}