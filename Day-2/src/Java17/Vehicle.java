// Sealed class allowing only specific subclasses
public sealed class Vehicle permits Bike, Car {
    public void start() {
        System.out.println("Vehicle is starting...");
    }
}
