package fun.jevon.thread.treadfun;

import java.util.concurrent.TimeUnit;

/**
 * 线程常用方法演示（wait / notify / notifyAll / sleep / join / yield / interrupt）
 *
 * 说明：
 * 1. wait()      -> 让当前线程进入 WAITING 状态，并释放对象锁，必须在同步块内使用
 *    notify()    -> 唤醒在当前对象监视器上等待的单个线程（任意一个）
 *    notifyAll() -> 唤醒在当前对象监视器上等待的所有线程
 *
 * 2. sleep(ms)   -> 让当前线程进入 TIMED_WAITING 状态，不释放锁
 *
 * 3. yield()     -> 提示调度器让出 CPU 时间片，线程重新竞争 CPU（不保证成功）
 *
 * 4. interrupt() -> 设置线程的中断标志。如果线程处于阻塞状态（如 sleep / wait / join），
 *                   会抛出 InterruptedException；否则仅设置标志位
 *
 * 5. join()      -> 等待另一个线程执行完毕，再继续当前线程（当前线程会进入 WAITING/TIMED_WAITING）
 */
public class ThreadFunctionsDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 线程常用方法演示 ===\n");

        demoWaitNotify();
        System.out.println("=".repeat(50));

        demoSleep();
        System.out.println("=".repeat(50));

        demoYield();
        System.out.println("=".repeat(50));

        demoInterrupt();
        System.out.println("=".repeat(50));

        demoJoin();
        System.out.println("=".repeat(50));

        System.out.println("演示完成。");
    }

    /**
     * 演示 wait / notify / notifyAll
     */
    private static void demoWaitNotify() throws InterruptedException {
        System.out.println("【wait / notify / notifyAll 演示】");
        final Object monitor = new Object();

        Thread waiter1 = new Thread(() -> {
            synchronized (monitor) {
                try {
                    System.out.println(Thread.currentThread().getName() + " 获取锁并进入 wait()");
                    monitor.wait(); // 进入 WAITING，释放锁
                    System.out.println(Thread.currentThread().getName() + " 被唤醒，继续执行");
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " 被中断");
                    Thread.currentThread().interrupt();
                }
            }
        }, "Demo-Waiter-1");

        Thread waiter2 = new Thread(() -> {
            synchronized (monitor) {
                try {
                    System.out.println(Thread.currentThread().getName() + " 获取锁并进入 wait()");
                    monitor.wait();
                    System.out.println(Thread.currentThread().getName() + " 被唤醒，继续执行");
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " 被中断");
                    Thread.currentThread().interrupt();
                }
            }
        }, "Demo-Waiter-2");

        Thread notifier = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ignored) {
            }
            synchronized (monitor) {
                System.out.println(Thread.currentThread().getName() + " 获取锁，调用 notify()");
                monitor.notify(); // 唤醒一个等待线程
            }

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ignored) {
            }
            synchronized (monitor) {
                System.out.println(Thread.currentThread().getName() + " 再次获取锁，调用 notifyAll()");
                monitor.notifyAll(); // 唤醒剩余等待线程
            }
        }, "Demo-Notifier");

        waiter1.start();
        waiter2.start();
        notifier.start();

        waiter1.join();
        waiter2.join();
        notifier.join();
    }

    /**
     * 演示 sleep
     */
    private static void demoSleep() throws InterruptedException {
        System.out.println("【sleep 演示】");
        Thread sleeper = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 开始执行，进入 sleep(1000)");
            try {
                Thread.sleep(1000); // TIMED_WAITING，不释放锁
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " 在 sleep 中被中断");
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " sleep 结束，继续运行");
        }, "Demo-Sleeper");

        sleeper.start();
        sleeper.join();
    }

    /**
     * 演示 yield让cpu时间片
     */
    private static void demoYield() throws InterruptedException {
        System.out.println("【yield 演示】");
        Runnable yieldTask = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + " 执行第 " + i + " 次，调用 yield()");
                Thread.yield(); // 提示调度器让出 CPU，重新竞争
            }
        };

        Thread t1 = new Thread(yieldTask, "Demo-Yield-1");
        Thread t2 = new Thread(yieldTask, "Demo-Yield-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    /**
     * 演示 interrupt中断
     */
    private static void demoInterrupt() throws InterruptedException {
        System.out.println("【interrupt 演示】");
        Thread worker = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 开始执行，准备 sleep(3000)");
            try {
                Thread.sleep(3000); // TIMED_WAITING
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " 在 sleep 中被 interrupt 标记唤醒");
                System.out.println("isInterrupted=" + Thread.currentThread().isInterrupted()); // =false（被清除）
                Thread.currentThread().interrupt(); // 重新设置中断标志
            }
            System.out.println(Thread.currentThread().getName() + " 结束，最终 isInterrupted=" + Thread.currentThread().isInterrupted());
        }, "Demo-Interrupt-Worker");

        worker.start();
        // 让当前线程暂停执行指定时间
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("主线程调用 worker.interrupt()");
        worker.interrupt(); // 设置中断标志，若处于 sleep 会抛异常
        worker.join();
    }

    /**
     * 演示 join
     */
    private static void demoJoin() throws InterruptedException {
        System.out.println("【join 演示】");// 1
        Thread slowWorker = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 执行耗时任务...");// 3
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " 任务完成");
        }, "Demo-Join-Worker");

        slowWorker.start();
        System.out.println("主线程调用 slowWorker.join()，等待其执行完毕");// 2（因为这是主线程的）
        slowWorker.join(); // 当前线程（主线程）等待 slowWorker 完成
        System.out.println("主线程检测到 slowWorker 结束，继续执行");
    }
}


