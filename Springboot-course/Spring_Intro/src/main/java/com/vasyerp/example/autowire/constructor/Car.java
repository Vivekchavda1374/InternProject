package com.vasyerp.example.autowire.constructor;

public class Car {
    private com.vasyerp.example.autowire.constructor.Specification specification;

    public Car(com.vasyerp.example.autowire.constructor.Specification specification) {
        this.specification = specification;
    }

//    public void setSpecification(Specification specification) {
//        this.specification = specification;
//    }

    public void displayDetails(){
        System.out.println("Car Details: " + specification.toString());
    }
}
