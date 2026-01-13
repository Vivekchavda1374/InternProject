package LamdaFunctionsAndStreamsApi;

import java.io.FileWriter;
import java.io.IOException;

public class Writer {


    public static void main(String[] args) {
//        try(FileWriter writer = new FileWriter("output1.txt")){
//            writer.write("Flushed output.");
//            writer.flush();
//            System.out.println("Output flushed.");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        try(FileWriter fileWriter = new FileWriter("output2.txt")){
            fileWriter.write("Hello World");
            System.out.println("String written successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
