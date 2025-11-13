package fun.jevon.thread.threadSafety;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 局部性（Locality）最简单演示
 *
 * 要点：
 * 1. 方法内局部变量：天然线程私有（每个线程有自己的栈帧副本）。
 * 2. ThreadLocal：为每个线程提供独立副本，避免共享带来的同步问题。
 */
public class LocalityDemo {

    // 使用 ThreadLocal 为每个线程准备独立的 StringBuilder
    // ThreadLocal<StringBuilder> 表示：每个线程都有自己独立的 StringBuilder 实例，互不干扰，不需要加锁。java自带
    // withInitial 用于定义每个线程第一次访问 ThreadLocal 时的初始化策略；
    // 这里用 StringBuilder::new 让每个线程懒加载独立的 StringBuilder 实例，并通过 remove() 防止线程池复用造成内存滞留。
    // 为每个线程准备一个 ThreadLocal 变量，当线程第一次调用 local.get() 时，才创建一个新的 StringBuilder 并绑定给该线程。
    private static final ThreadLocal<StringBuilder> localBuilder = ThreadLocal.withInitial(StringBuilder::new);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 局部性（Locality）最简单演示 ===\n");

        ExecutorService pool = Executors.newFixedThreadPool(5);

        // 提交 5 个任务，每个任务演示：
        // 1) 方法内局部变量独立
        // 2) ThreadLocal 独立副本
        for (int i = 1; i <= 5; i++) {
            final int threadId = i;
            pool.submit(() -> {
                // 1) 方法内局部变量：每个线程有自己的副本，无需同步
                int localSum = 0;
                for (int j = 0; j < 1000; j++) {
                    localSum += j;
                }
                System.out.println("线程-" + threadId + " 的方法内局部变量 localSum = " + localSum);

                // 2) ThreadLocal：每个线程拿到独立的 StringBuilder，不会互相干扰
                StringBuilder sb = localBuilder.get();
                for (int j = 0; j < 1000; j++) {
                    sb.append('X');
                }
                System.out.println("线程-" + threadId + " 的ThreadLocal长度 = " + sb.length());

                // 可选：清理 ThreadLocal，避免线程池复用时持有大对象
                localBuilder.remove();
            });
        }

        pool.shutdown();
        // ExecutorService 提供的一个“阻塞等待线程池任务执行完毕”的方法。
        pool.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\n说明：方法内局部变量与 ThreadLocal 都属于线程私有数据，\n" +
                "它们不需要加锁即可避免竞态条件，从而保证线程安全。");
    }
}


