package LamdaFunctionsAndStreamsApi;


import java.io.*;

class Student implements Serializable{
    int id;
    String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
public class ObjectStreamers {
    public static void main(String[] args) {
        Student student = new Student(1, "John");

//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("student.ser"))) {
//            oos.writeObject(student);
//            System.out.println("Object has been serialized.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("student.ser"))){
//            student = (Student) objectInputStream.readObject();
//            System.out.println("ID: " + student.id);
//            System.out.println("Name: " + student.name);
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }


    }
}
