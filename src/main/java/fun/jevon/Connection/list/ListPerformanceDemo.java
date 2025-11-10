package fun.jevon.Connection.list;

import java.util.*;
import java.util.concurrent.*;

public class ListPerformanceDemo {
    private static final int N = 10_000000; // 每个线程添加元素数量
    private static final int THREADS = 10; // 并发线程数

    public static void main(String[] args) throws InterruptedException {
        // 单线程性能测试
        testPerformance(new ArrayList<>(), "ArrayList 单线程");
        testPerformance(new Vector<>(), "Vector 单线程");
        testPerformance(new LinkedList<>(), "LinkedList 单线程");

        // 线程不安全体验
        testThreadSafety(new ArrayList<>(), "ArrayList（线程不安全）", false);
        testThreadSafety(new LinkedList<>(), "LinkedList（线程不安全）", false);

        // 线程安全体验
        testThreadSafety(new Vector<>(), "Vector（线程安全）", true);
    }

    private static void testPerformance(List<Integer> list, String name) {
        long start = System.nanoTime();
        for (int i = 0; i < N; i++) list.add(i);
        long end = System.nanoTime();
        System.out.printf("%s add()耗时: %d ms%n", name, (end - start) / 1_000_000);
    }

    private static void testThreadSafety(List<Integer> list, String name, boolean safe)
            throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        long start = System.nanoTime();

        for (int i = 0; i < THREADS; i++) {
            pool.execute(() -> {
                for (int j = 0; j < N; j++) {
                    list.add(j);
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        long end = System.nanoTime();

        int expected = THREADS * N;
        System.out.printf("%s耗时: %d ms, 大小= %d, %s%n",
                name, (end - start) / 1_000_000, list.size(),
                safe ? "线程安全" : "线程不安全");
    }
}
