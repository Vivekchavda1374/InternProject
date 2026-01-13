package MultiThreading;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueue {
    public static void main(String[] args) throws InterruptedException{

//        java.util.concurrent.BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(3);
//
//        queue.put(1);
//        queue.put(2);
//        queue.put(3);
//
//        System.out.println("BlockingQueue: " + queue);
        java.util.concurrent.BlockingQueue<Integer> queue = new LinkedBlockingDeque<>();

        queue.add(166);
        queue.add(246);
        queue.add(66);
        queue.add(292);
        queue.add(98);

        Iterator<Integer> iterator = queue.iterator();

        System.out.println("BlockingQueue elements:");
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
    }
}
