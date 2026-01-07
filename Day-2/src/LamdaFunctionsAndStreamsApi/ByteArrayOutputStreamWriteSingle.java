package LamdaFunctionsAndStreamsApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteArrayOutputStreamWriteSingle {
    public static void main(String[] args) {
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//            baos.write(65);
//            System.out.println(baos);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }



//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//            String msg = "Hello Java";
//            baos.write(msg.getBytes());
//            System.out.println(baos.toString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }




//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//            baos.write("Hello world".getBytes());
//            byte[] result = baos.toByteArray();
//            for (byte b : result) {
//                System.out.print((char) b);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            baos.write("Before Reset".getBytes());
            baos.reset();
            baos.write("After Reset".getBytes());
            System.out.println(baos.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
