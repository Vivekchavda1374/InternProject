package LamdaFunctionsAndStreamsApi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

class Person {
    public Person() {
        System.out.println("Person object created");
    }
}

public class LambdaBasics {

    public static void main(String[] args){
//        helloWorld hello =new helloWorld() {
//            @Override
//            public void helloWorld() {
//                System.out.println("Hello World");
//            }
//        };
//        hello.helloWorld();
//
//        helloWorld hello =() ->{
//            System.out.println("Hello World");
//        };
//        hello.helloWorld();


//        helloWorld helloWorld = () -> {
//            System.out.println("Hello");
//        };

//
//        nameStudent nameStudent = (name )->{
//            System.out.println("Hello "+name);
//        };
//        nameStudent.nameStudent("Vivek");


//            Math math = (a,b) ->{
//                return a+b;
//            };
//        System.out.println(math.sum(4,5));

//        List<String> name = Arrays.asList("Vivek","ERP","Customer");
//        Collections.sort(name,(s1,s2)->s2.compareToIgnoreCase(s1));
//        System.out.println(name);




//    String animalName = "Dog";
//    Animal animal = ()-> System.out.println(animalName);
//    animal.animalName();
//
//        AtomicInteger number = new AtomicInteger(10);
//        Runnable task = () -> number.incrementAndGet();
//        System.out.println(task);

//        Function<Integer,Integer> square=(sqare)->LambdaBasics.square(sqare);
//        Function<Integer,Integer> methodSquare = LambdaBasics::square;
//        System.out.println(square.apply(5));
//        System.out.println(methodSquare.apply(5));

//        Supplier<Person> lambdaPerson = () -> new Person();
//        Supplier<Person> methodPerson = Person::new;
//
//        lambdaPerson.get();
//        methodPerson.get();

    }

    public interface helloWorld{
        void helloWorld();
    }
    @FunctionalInterface
    public interface nameStudent{
        void nameStudent(String name);
    }

    @FunctionalInterface
    public interface Math{
        int sum(int a , int b);

    }
    @FunctionalInterface
    public interface Animal{
        void animalName();
    }

    public static int square(int number) {
        return number * number;
    }

}

