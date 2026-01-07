package MultiThreading;

class World extends Thread{
    public void run() {
        for (; ; ) {
            System.out.println("World");
        }
    }
}
//class World implements Runnable{
//    public void run(){
//        for(; ; ){
//            System.out.println("World");
//        }
//
//    }
//}
public class Main {
    public static void main(String[] args) {
        World world = new World();
        world.start();
        for (; ; ) {
            System.out.println("Hello");
        }
    }
}
