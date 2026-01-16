package com.vasyerp.example.autowire.constructor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context
                = new ClassPathXmlApplicationContext("autowireByConstructor.xml");
        com.vasyerp.example.autowire.constructor.Car myCar = (com.vasyerp.example.autowire.constructor.Car) context.getBean("myCar");
        myCar.displayDetails();
    }
}
