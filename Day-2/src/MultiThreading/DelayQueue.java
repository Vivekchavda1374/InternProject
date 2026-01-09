package MultiThreading;

// Java Program to demonstrates the working of DelayQueue
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueue {

    // Class that implements the Delayed interface
    static class MyDelayedElement implements Delayed {
        private final String name;
        private final long delayTime;
        private final long startTime;

        public MyDelayedElement(String name, long delayTime, TimeUnit unit) {
            this.name = name;

            // Convert delay to milliseconds
            this.delayTime = unit.toMillis(delayTime);
            this.startTime = System.currentTimeMillis();
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long diff = startTime + delayTime - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.MILLISECONDS),
                    o.getDelay(TimeUnit.MILLISECONDS));
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // Create a DelayQueue
        java.util.concurrent.DelayQueue<MyDelayedElement> d = new java.util.concurrent.DelayQueue<>();

        // Add elements with different delay times
        d.add(new MyDelayedElement("Task 1", 3, TimeUnit.SECONDS));
        d.add(new MyDelayedElement("Task 2", 1, TimeUnit.SECONDS));
        d.add(new MyDelayedElement("Task 3", 2, TimeUnit.SECONDS));

        // Wait and process elements as their delays expire
        while (!d.isEmpty()) {

            // This will block until an
            // element's delay has expired
            MyDelayedElement t = d.take();
            System.out.println("Executing: " + t.getName());
        }
    }
}