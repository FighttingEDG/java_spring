package fun.jevon.thread.threadSafety;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子性（Atomicity）最简单演示
 *
 * 要点：
 * 1. ++ 操作不是原子性的，多个线程同时执行会产生竞态，导致丢失更新。
 * 2. AtomicInteger 的自增（incrementAndGet）是原子操作，保证并发正确性。
 */
public class AtomicityDemo {

    // 非线程安全的计数器示例
    static class UnsafeCounter {
        private int value = 0; // 非原子

        public void increment() {
            // value++ 分为 读-改-写 三步，非原子，存在丢失更新风险
            value++;
        }

        public int get() {
            return value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 原子性（Atomicity）最简单演示 ===\n");

        final int threads = 10;
        final int perThreadIncr = 100_000;
        // 预期
        final int expected = threads * perThreadIncr;

        UnsafeCounter unsafeCounter = new UnsafeCounter();
        // 基于 CPU 的 CAS（Compare-And-Set）指令与内存语义，保证读-改-写的原子性与可见性
        // 原子整型计数器，java自带
        AtomicInteger atomicCounter = new AtomicInteger(0);

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int i = 1; i <= threads; i++) {
            final int threadId = i;
            pool.submit(() -> {
                for (int j = 0; j < perThreadIncr; j++) {
                    // 非原子自增：会丢失更新
                    unsafeCounter.increment();
                    // 原子自增：保证正确
                    atomicCounter.incrementAndGet();
                }
                if (threadId == 1) {
                    // 仅示例打印一次原子计数器的中间值
                    System.out.println("示例中间值（原子计数器）= " + atomicCounter.get());
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\n预期计数: " + expected);
        System.out.println("非原子计数器结果: " + unsafeCounter.get());
        System.out.println("原子计数器结果: " + atomicCounter.get());
        System.out.println("非原子是否丢失更新: " + (unsafeCounter.get() < expected));
        System.out.println("原子是否正确: " + (atomicCounter.get() == expected));
    }
}


