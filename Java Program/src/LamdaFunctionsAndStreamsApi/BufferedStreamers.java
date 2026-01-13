package LamdaFunctionsAndStreamsApi;

import java.io.*;

public class BufferedStreamers {
    public static void main(String[] args) throws RuntimeException {
//        byte[] buffer = new byte[20];
//        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("hello.txt"))) {
//            int data;
////            while ((data = bis.read()) != -1) {
////                System.out.print((char) data);
////                }
//            int bytesRead = bis.read(buffer, 0, buffer.length);
//            System.out.println("Read " + bytesRead + " bytes: " + new String(buffer, 0, bytesRead));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("output.txt"))) {
//            bos.write(66);
//            bos.flush();
//            System.out.println("Single byte written successfully.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String data = "Buffered Output";
        byte[] bytes = data.getBytes();

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("output.txt"))) {
            bos.write(bytes, 0, bytes.length);
            bos.flush();
            System.out.println("Byte array written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
