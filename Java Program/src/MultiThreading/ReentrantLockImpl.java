package MultiThreading;

import java.util.concurrent.locks.Lock;

public class ReentrantLockImpl {
    private final Lock lock = new java.util.concurrent.locks.ReentrantLock();

    public void outerMethod() {
        lock.lock();
        try {
            System.out.println("Outer method");
            innerMethod();
        } finally {
            lock.unlock();
        }
    }

    public void innerMethod() {
        lock.lock();
        try {
            System.out.println("Inner method");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockImpl example = new ReentrantLockImpl();
        example.outerMethod();
    }
}
