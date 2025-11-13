package fun.jevon.thread.threadSafety;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 互斥锁（Mutex）最简单演示
 *
 * 要点：
 * 1. 多线程并发写共享可变状态时，需要互斥（如 synchronized 或 ReentrantLock）。
 * 2. 未加锁的自增存在竞态条件，最终结果通常小于期望值。
 * 3. 加锁后串行化临界区，保证可见性与原子性。
 */
public class MutexDemo {

    // 未加锁的共享计数器（有竞态风险）
    private static int unsafeCounter = 0;

    // 使用互斥保护的共享计数器
    private static int safeCounter = 0;
    // Lock 是一个接口，定义了加锁与解锁的标准行为
    // ReentrantLock 是它的一个具体实现类，是可重入的、可中断的、可公平的锁，比 synchronized 更灵活
    private static final Lock lock = new ReentrantLock();

    // 也可用 synchronized 保护：
    // synchronized 是 Java 最基础的线程同步关键字，通过互斥（mutual exclusion）方式串行化临界区的执行，从而保证线程安全。
    private static synchronized void synchronizedInc() {
        safeCounter++;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 互斥锁（Mutex）最简单演示 ===\n");

        int threads = 5;
        int incrementsPerThread = 100_000;// 相当于100000
        int expected = threads * incrementsPerThread;

        ExecutorService pool = Executors.newFixedThreadPool(threads);

        // 提交任务：同时对两个计数器做相同次数的自增
        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    // 1) 未加锁：存在竞态条件
                    unsafeCounter++;

                    // 2) 加锁方案一：显式 ReentrantLock
                    // 当前线程获取锁后执行 try 块中的代码，这段代码是临界区。finally 确保锁在执行完成或异常时释放，这是使用 ReentrantLock 的标准模式
                    lock.lock();
                    try {
                        safeCounter++;
                    } finally {
                        // 不管过程是否报错都释放锁，防止死锁
                        lock.unlock();
                    }

                    // 3) 加锁方案二（可替换上面 lock 的实现）：synchronized 方法
//                     synchronizedInc();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("期望值(expected): " + expected);
        System.out.println("未加锁结果(unsafeCounter): " + unsafeCounter);
        System.out.println("加锁结果(safeCounter):   " + safeCounter);

        System.out.println("\n说明：未加锁的自增在并发下会丢失更新；使用互斥锁或 synchronized 可保证正确性，但会带来串行化的开销。");
    }
}


