// Permitted subclass
final class Car extends Vehicle {
    public void drive() {
        System.out.println("Car is driving...");
    }
}

// Permitted subclass
non-sealed class Bike extends Vehicle {
    public void ride() {
        System.out.println("Bike is riding...");
    }
}


public class SealedClass {
    public static void main(String[] args) {
        Vehicle car = new Car();
        car.start(); // ✅ Allowed
        ((Car) car).drive(); // ✅ Allowed

        Vehicle bike = new Bike();
        bike.start(); // ✅ Allowed
        ((Bike) bike).ride(); // ✅ Allowed
    }
}