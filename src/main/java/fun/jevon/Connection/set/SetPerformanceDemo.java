package fun.jevon.Connection.set;

import java.util.*;
import java.util.concurrent.*;

public class SetPerformanceDemo {
    private static final int N = 100_000;  // 每个线程添加元素数量
    private static final int THREADS = 10;  // 并发线程数

    public static void main(String[] args) throws InterruptedException {
        // 单线程性能测试
        testPerformance(new HashSet<>(), "HashSet 单线程");
        testPerformance(new LinkedHashSet<>(), "LinkedHashSet 单线程");
        testPerformance(new TreeSet<>(), "TreeSet 单线程");

        // 线程不安全体验
        testThreadSafety(new HashSet<>(), "HashSet（线程不安全）", false);
        testThreadSafety(new LinkedHashSet<>(), "LinkedHashSet（线程不安全）", false);
        testThreadSafety(new TreeSet<>(), "TreeSet（线程不安全）", false);

        // 线程安全体验
        testThreadSafety(Collections.synchronizedSet(new HashSet<>()),
                "Synchronized HashSet（线程安全）", true);
    }

    private static void testPerformance(Set<Integer> set, String name) {
        long start = System.nanoTime();
        for (int i = 0; i < N; i++) set.add(i);
        long end = System.nanoTime();
        System.out.printf("%s add()耗时: %d ms%n", name, (end - start) / 1_000_000);
    }

    private static void testThreadSafety(Set<Integer> set, String name, boolean safe)
            throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        long start = System.nanoTime();

        for (int i = 0; i < THREADS; i++) {
            // 提交一个任务给线程池执行
            pool.execute(() -> {
                for (int j = 0; j < N; j++) {
                    set.add(j);
                }
            });
        }


        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        long end = System.nanoTime();

        System.out.printf("%s耗时: %d ms, 大小= %d, %s%n",
                name, (end - start) / 1_000_000, set.size(),
                safe ? "线程安全" : "线程不安全");
    }
}
