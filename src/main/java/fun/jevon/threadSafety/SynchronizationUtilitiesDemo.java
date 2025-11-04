package fun.jevon.threadSafety;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 同步工具（Synchronization Utilities）最简单演示
 *
 * 要点：
 * 1. CountDownLatch：等待多个任务完成，一次性的倒计时门闩。
 * 2. Semaphore：控制并发访问资源的线程数量，信号量。
 */
public class SynchronizationUtilitiesDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 同步工具（Synchronization Utilities）最简单演示 ===\n");

        demoCountDownLatch();
        System.out.println();
        demoSemaphore();
    }

    // 演示 CountDownLatch：等待多个任务全部完成
    private static void demoCountDownLatch() throws InterruptedException {
        System.out.println("1) CountDownLatch 演示：等待多个任务全部完成后继续执行");

        final int tasks = 5;
        // CountDownLatch线程协调工具类，让多个线程同步起跑或等待彼此完成
        // 每当有线程调用一次 countDown()，计数器就减 1
        // 当计数器减到 0 时，所有在 await() 上等待的线程会被同时唤醒
        CountDownLatch latch = new CountDownLatch(tasks);

        ExecutorService pool = Executors.newFixedThreadPool(tasks);
        for (int i = 1; i <= tasks; i++) {
            final int taskId = i;
            pool.submit(() -> {
                try {
                    // 模拟任务执行时间
                    Thread.sleep(1000 + taskId * 100);
                    System.out.println("任务-" + taskId + " 完成");
                } catch (InterruptedException e) {
                    // 捕获 Thread.sleep() 的中断异常
                    // 恢复中断状态（这是并发编程的良好实践）
                    // 保证线程的中断信号不丢失
                    Thread.currentThread().interrupt();
                } finally {
                    // 任务完成后，倒计时减1
                    latch.countDown();
                }
            });
        }

        System.out.println("主线程等待所有任务完成...");
        // 主线程阻塞，直到倒计时归零（所有任务完成）
        latch.await();
        System.out.println("所有任务已完成，主线程继续执行");

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    // 演示 Semaphore：限制并发访问资源的线程数量
    // 线程池大小：10，但 Semaphore 限制了实际并行访问资源的线程数
    private static void demoSemaphore() throws InterruptedException {
        System.out.println("2) Semaphore 演示：限制同时访问资源的线程数量（例如：最多3个线程同时访问）");

        final int totalThreads = 10;
        final int permits = 3; // 最多允许3个线程同时访问
        // Semaphore信号量
        // 控制同时能访问某个资源的线程数量
        Semaphore semaphore = new Semaphore(permits);

        ExecutorService pool = Executors.newFixedThreadPool(totalThreads);
        for (int i = 1; i <= totalThreads; i++) {
            final int threadId = i;
            // 这里一直提交任务
            pool.submit(() -> {
                try {
                    // 获取许可（如果没有 可用 许可，会阻塞等待）
                    // 相当于限制了同时能提交多少个任务进线程池
                    semaphore.acquire();
                    // availablePermits返回当前剩余的许可数
                    System.out.println("线程-" + threadId + " 获得许可，开始访问资源（当前并发数：" + (permits - semaphore.availablePermits()) + "）");
                    
                    // 模拟资源访问
                    Thread.sleep(2000);
                    
                    System.out.println("线程-" + threadId + " 释放许可");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // 释放一个许可
                    semaphore.release();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
        
        System.out.println("说明：Semaphore 通过信号量控制并发度，适合限流、资源池等场景。");
    }
}

