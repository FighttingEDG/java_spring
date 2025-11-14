package fun.jevon.thread.waitAndSleep;

/**
 * wait() 和 sleep() 的区别演示
 *
 * 主要区别：
 * 1. 来自不同的类：
 *    - wait(): 来自 Object 类
 *    - sleep(): 来自 Thread 类
 *
 * 2. 关于锁的释放：
 *    - wait(): 在等待的过程中会释放锁
 *    - sleep(): 在等待的过程中不会释放锁
 *
 * 3. 使用的范围：
 *    - wait(): 必须在同步代码块中使用
 *    - sleep(): 可以在任何地方使用
 *
 * 4. 是否需要捕获异常：
 *    - wait(): 需要捕获 InterruptedException（实际上需要）
 *    - sleep(): 需要捕获 InterruptedException
 */
public class WaitAndSleepDemo {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== wait() 和 sleep() 的区别演示 ===\n");

        // ========== 区别1：来自不同的类 ==========
        System.out.println("【区别1：来自不同的类】\n");
        System.out.println("wait() 方法：");
        System.out.println("  - 来自 Object 类");
        System.out.println("  - 所有对象都可以调用 wait() 方法");
        System.out.println("  - 示例：lock.wait()");
        System.out.println("\nsleep() 方法：");
        System.out.println("  - 来自 Thread 类");
        System.out.println("  - 只有线程对象可以调用 sleep() 方法");
        System.out.println("  - 示例：Thread.sleep(1000)");
        System.out.println("\n" + "=".repeat(50) + "\n");

        // ========== 区别2：关于锁的释放 ==========
        System.out.println("【区别2：关于锁的释放】\n");

        // wait() 会释放锁
        System.out.println("1. wait() 会释放锁：");
        Thread waitThread = new Thread(() -> {
            synchronized (lock) {
                // 进来了就说明持有锁了
                System.out.println("  Demo-Wait-Thread 获取到锁");
                try {
                    System.out.println("  Demo-Wait-Thread 调用 wait()，释放锁");
                    lock.wait(2000); // wait 会释放锁
                    System.out.println("  Demo-Wait-Thread 被唤醒，重新获取锁");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Demo-Wait-Thread");

        Thread notifyThread = new Thread(() -> {
            try {
                Thread.sleep(500); // 确保 waitThread 先获取锁
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock) {
                System.out.println("  Demo-Notify-Thread 获取到锁（说明 wait() 释放了锁）");
                lock.notify(); // 唤醒等待的线程
            }
        }, "Demo-Notify-Thread");

        waitThread.start();
        notifyThread.start();
        waitThread.join();
        notifyThread.join();

        System.out.println("\n2. sleep() 不会释放锁：");
        Thread sleepThread = new Thread(() -> {
            synchronized (lock) {
                System.out.println("  Demo-Sleep-Thread 获取到锁");
                try {
                    System.out.println("  Demo-Sleep-Thread 调用 sleep()，不释放锁");
                    Thread.sleep(1000); // sleep 不会释放锁
                    System.out.println("  Demo-Sleep-Thread sleep() 结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Demo-Sleep-Thread");

        Thread tryLockThread = new Thread(() -> {
            try {
                Thread.sleep(100); // 确保 sleepThread 先获取锁
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("  Demo-TryLock-Thread 尝试获取锁...");
            synchronized (lock) {
                System.out.println("  Demo-TryLock-Thread 获取到锁（说明 sleep() 没有释放锁，需要等待）");
            }
        }, "Demo-TryLock-Thread");

        sleepThread.start();
        tryLockThread.start();
        sleepThread.join();
        tryLockThread.join();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // ========== 区别3：使用的范围 ==========
        System.out.println("【区别3：使用的范围】\n");

        System.out.println("1. wait() 必须在同步代码块中使用：");
        System.out.println("   - 如果不在 synchronized 块中使用，会抛出 IllegalMonitorStateException");
        Thread waitInSync = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait(500); // 在同步块中使用，正常
                    System.out.println("  ✓ wait() 在同步代码块中使用正常");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Demo-Wait-In-Sync");
        waitInSync.start();
        waitInSync.join();

        System.out.println("\n2. sleep() 可以在任何地方使用：");
        Thread sleepAnywhere = new Thread(() -> {
            try {
                Thread.sleep(500); // 可以在任何地方使用
                System.out.println("  ✓ sleep() 在任何地方使用都正常");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Demo-Sleep-Anywhere");
        sleepAnywhere.start();
        sleepAnywhere.join();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // ========== 区别4：是否需要捕获异常 ==========
        System.out.println("【区别4：是否需要捕获异常】\n");

        System.out.println("1. wait() 需要捕获 InterruptedException：");
        Thread waitWithException = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait(500); // 需要捕获 InterruptedException
                    System.out.println("  ✓ wait() 需要捕获 InterruptedException");
                } catch (InterruptedException e) {
                    System.out.println("  ✓ wait() 捕获到 InterruptedException");
                }
            }
        }, "Demo-Wait-Exception");
        waitWithException.start();
        waitWithException.join();

        System.out.println("\n2. sleep() 需要捕获 InterruptedException：");
        Thread sleepWithException = new Thread(() -> {
            try {
                Thread.sleep(500); // 需要捕获 InterruptedException
                System.out.println("  ✓ sleep() 需要捕获 InterruptedException");
            } catch (InterruptedException e) {
                System.out.println("  ✓ sleep() 捕获到 InterruptedException");
            }
        }, "Demo-Sleep-Exception");
        sleepWithException.start();
        sleepWithException.join();

        // ========== 总结 ==========
        System.out.println("\n" + "=".repeat(50));
        System.out.println("\n【总结】");
        System.out.println("1. 来自不同的类：");
        System.out.println("   - wait(): Object 类");
        System.out.println("   - sleep(): Thread 类");
        System.out.println("\n2. 关于锁的释放：");
        System.out.println("   - wait(): 会释放锁");
        System.out.println("   - sleep(): 不会释放锁");
        System.out.println("\n3. 使用的范围：");
        System.out.println("   - wait(): 必须在同步代码块中使用");
        System.out.println("   - sleep(): 可以在任何地方使用");
        System.out.println("\n4. 是否需要捕获异常：");
        System.out.println("   - wait(): 需要捕获 InterruptedException");
        System.out.println("   - sleep(): 需要捕获 InterruptedException");
    }
}

