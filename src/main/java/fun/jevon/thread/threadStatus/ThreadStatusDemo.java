package fun.jevon.thread.threadStatus;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程状态演示
 *
 * 线程的几种状态：
 * 1. NEW - 新建状态：生成线程对象，但没有调用 start() 方法
 * 2. RUNNABLE - 就绪/运行状态：调用了 start() 方法，等待被调度或正在运行
 * 3. BLOCKED - 阻塞状态：线程在获取 synchronized 同步锁失败
 * 4. WAITING - 等待状态：通过调用 wait() 方法，让线程等待
 * 5. TIMED_WAITING - 超时等待状态：通过 sleep()、join() 或 I/O 请求
 * 6. TERMINATED - 死亡状态：线程执行完毕或异常退出
 *
 * status枚举是只有这六种状态，有时候教材会有running，代表cpu调度执行的那一瞬间
 */
public class ThreadStatusDemo {

    private static final Object lock = new Object();
    // ReentrantLock是Lock的具体实现类，可重入锁功能
    // 同一个线程可以多次获取同一把锁而不会死锁
    private static final Lock reentrantLock = new ReentrantLock();

    /**
     * 主流程：依次演示 NEW → RUNNABLE → TIMED_WAITING(wait/join) → WAITING → BLOCKED → TERMINATED
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 线程状态演示 ===\n");

        // ========== 状态1：NEW - 新建状态 ==========
        // 创建线程对象但不调用 start()，线程保持 NEW
        System.out.println("【状态1：NEW - 新建状态】");
        Thread thread1 = new Thread(() -> {
            System.out.println("线程执行");
        }, "Demo-Thread-1");
        System.out.println("创建线程对象后，未调用 start()");
        System.out.println("状态：" + thread1.getState()); // NEW
        System.out.println("说明：生成线程对象，但没有调用 start() 方法\n");

        // ========== 状态2：RUNNABLE - 就绪/运行状态 ==========
        // 调用 start() 后线程进入 RUNNABLE；此处稍等后查看状态（可能已 TERMINATED）
        System.out.println("【状态2：RUNNABLE - 就绪/运行状态】");
        Thread thread2 = new Thread(() -> {
            System.out.println("  线程正在运行，当前状态：" + Thread.currentThread().getState());
        }, "Demo-Thread-2");
        thread2.start();
        // 调用start是runnable，start结束后是terminated，已终止
        // sleep其实是TIMED_WAITING超时等待状态，但是这里打印不了
        Thread.sleep(100); // 等待线程执行
        System.out.println("调用 start() 后，线程状态：" + thread2.getState()); // TERMINATED（已执行完）
        System.out.println("说明：调用了 start() 方法后，线程进入 RUNNABLE 状态（就绪或运行）\n");

        // ========== 状态3：TIMED_WAITING - 超时等待状态（sleep） ==========
        // Thread.sleep() 会让线程进入 TIMED_WAITING 且不释放锁
        System.out.println("【状态3：TIMED_WAITING - 超时等待状态（sleep）】");
        Thread thread3 = new Thread(() -> {
            try {
                Thread.sleep(2000); // sleep 进入 TIMED_WAITING
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Demo-Thread-3");
        thread3.start();
        Thread.sleep(100); // 等待线程进入 sleep
        System.out.println("线程调用 sleep() 后：" + thread3.getState()); // TIMED_WAITING
        thread3.join();
        System.out.println("说明：通过 sleep() 方法，线程进入 TIMED_WAITING 状态\n");

        // ========== 状态4：TIMED_WAITING - 超时等待状态（join） ==========
        // join() 期间调用线程进入 TIMED_WAITING，直到被等待线程结束或超时
        System.out.println("【状态4：TIMED_WAITING - 超时等待状态（join）】");
        Thread thread4 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Demo-Thread-4");
        thread4.start();
        Thread mainThread = Thread.currentThread();
        Thread watcher = new Thread(() -> {
            try {
                thread4.join(2000); // join 进入 TIMED_WAITING
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Demo-Watcher");
        watcher.start();
        Thread.sleep(50);
        System.out.println("线程调用 join() 后：" + watcher.getState()); // TIMED_WAITING
        watcher.join();
        thread4.join();
        System.out.println("说明：通过 join() 方法，线程进入 TIMED_WAITING 状态\n");

        // ========== 状态5：WAITING - 等待状态（wait） ==========
        // wait() 需要在同步块中调用，调用后释放锁并进入 WAITING，等待 notify/notifyAll
        System.out.println("【状态5：WAITING - 等待状态（wait）】");
        Thread thread5 = new Thread(() -> {
            synchronized (lock) {
                try {
                    // 所有对象都继承自object，所以有wait方法，让当前线程进入wait状态
                    lock.wait(); // wait 进入 WAITING
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Demo-Thread-5");
        thread5.start();
        Thread.sleep(100); // 等待线程进入 wait
        System.out.println("线程调用 wait() 后：" + thread5.getState()); // WAITING
        synchronized (lock) {
            lock.notify(); // 唤醒等待 lock 的一个线程
        }
        thread5.join();
        System.out.println("说明：通过 wait() 方法，线程进入 WAITING 状态\n");

        // ========== 状态6：BLOCKED - 阻塞状态（synchronized） ==========
        // 当线程尝试获取被占用的 synchronized 锁时，会进入 BLOCKED
        System.out.println("【状态6：BLOCKED - 阻塞状态（synchronized）】");
        Thread thread6 = new Thread(() -> {
            synchronized (lock) {
                try {
                    Thread.sleep(1000); // 持有锁一段时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Demo-Thread-6");
        Thread thread7 = new Thread(() -> {
            synchronized (lock) { // 尝试获取锁，但被 thread6 占用
                System.out.println("Demo-Thread-7 获取到锁");
            }
        }, "Demo-Thread-7");
        thread6.start();
        Thread.sleep(50); // 确保 thread6 先获取锁
        thread7.start();
        Thread.sleep(100); // 等待 thread7 尝试获取锁
        // 还没拿到锁，被thread6占用了
        System.out.println("线程尝试获取 synchronized 锁失败后：" + thread7.getState()); // BLOCKED
        thread6.join();
        thread7.join();
        System.out.println("说明：线程7在获取 synchronized 同步锁失败时，进入 BLOCKED 状态\n");

        // ========== 状态7：TERMINATED - 死亡状态 ==========
        // run() 执行完毕或抛异常后，线程进入 TERMINATED
        System.out.println("【状态7：TERMINATED - 死亡状态】");
        Thread thread8 = new Thread(() -> {
            System.out.println("线程执行完毕");
        }, "Demo-Thread-8");
        thread8.start();
        thread8.join();
        System.out.println("线程执行完毕后：" + thread8.getState()); // TERMINATED
        System.out.println("说明：线程执行完毕或异常退出后，进入 TERMINATED 状态\n");

        // ========== 状态转换总结 ==========
        System.out.println("=".repeat(50));
        System.out.println("\n【状态转换总结】");
        System.out.println("1. NEW -> RUNNABLE：调用 start() 方法");
        System.out.println("2. RUNNABLE -> TIMED_WAITING：调用 sleep()、join() 或 I/O 请求");
        System.out.println("3. RUNNABLE -> WAITING：调用 wait() 方法");
        System.out.println("4. RUNNABLE -> BLOCKED：获取 synchronized 锁失败");
        System.out.println("5. TIMED_WAITING -> RUNNABLE：sleep() 超时、join() 完成或 I/O 完成");
        System.out.println("6. WAITING -> RUNNABLE：调用 notify() 或 notifyAll()");
        System.out.println("7. BLOCKED -> RUNNABLE：获取到 synchronized 锁");
        System.out.println("8. RUNNABLE -> TERMINATED：线程执行完毕或异常退出");
    }
}

