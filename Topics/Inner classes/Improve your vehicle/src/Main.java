class Vehicle {

    private String name;

    public Vehicle(String name) {
        this.name = name;
    }

    class Engine {

        private int horsepower;

        public Engine(int horsepower) {
            this.horsepower = horsepower;
        }

        void start() {
            System.out.println("RRRrrrrrrr....");
        }

        public void printHorsePower() {
            System.out.println("Vehicle " + Vehicle.this.name + " has " +horsepower + " horsepower.");
        }
    }
}

// this code should work
class EnjoyVehicle {

    public static void main(String[] args) {

        Vehicle vehicle = new Vehicle("Dixi");
        Vehicle.Engine engine = vehicle.new Engine(15);
        engine.printHorsePower();
    }
}