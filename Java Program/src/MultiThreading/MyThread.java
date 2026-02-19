package MultiThreading;

public class MyThread extends Thread {

//    public void run() {
//        System.out.println("RUNNING"); // RUNNING
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            System.out.println(e);
//        }
    public MyThread(){}
    public MyThread(String name) {
        super(name);
    }
    @Override
    public void run() {
//        System.out.println("Thread is Running...");
//        for (int i = 1; i <= 5; i++) {
//            for (int j = 0; j < 5; j++) {
//                System.out.println(Thread.currentThread().getName() + " - Priority: " + Thread.currentThread().getPriority() + " - count: " + i);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//
//                }
//            }
//        }


//        for (int i = 0; i < 5; i++) {
//            System.out.println(Thread.currentThread().getName() + " is running...");
//            Thread.yield();
//        }

        while (true) {
            System.out.println("Hello world! ");
        }

    }

    public static void main(String[] args) throws InterruptedException {
//        MyThread t1 = new MyThread();
//        System.out.println(t1.getState()); // NEW
//        t1.start();
//        System.out.println(t1.getState()); // RUNNABLE
//        Thread.sleep(100);
//        System.out.println(t1.getState()); // TIMED_WAITING
//        t1.join();
//        System.out.println(t1.getState()); // TERMINATED


//        MyThread l = new MyThread("Low Priority Thread");
//        MyThread m = new MyThread("Medium Priority Thread");
//        MyThread n = new MyThread("High Priority Thread");
//        l.setPriority(Thread.MIN_PRIORITY);
//        m.setPriority(Thread.NORM_PRIORITY);
//        n.setPriority(Thread.MAX_PRIORITY);
//        l.start();
//        m.start();
//        n.start();

//        MyThread t1 = new MyThread();
//        MyThread t2 = new MyThread();
//        t1.start();
//        t2.start();



        MyThread myThread = new MyThread();
        myThread.setDaemon(true);
        MyThread t1 = new MyThread();
        t1.start();
        myThread.start();
        System.out.println("Main Done");
    }
}

