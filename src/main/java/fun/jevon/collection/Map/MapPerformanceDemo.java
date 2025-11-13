package fun.jevon.collection.Map;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 四种 Map（HashMap / LinkedHashMap / TreeMap / Hashtable）
 * - 单线程性能（put/get）对比
 * - 并发写入下的线程安全性验证
 *
 * 结论（典型特性）：
 * - HashMap：非线程安全；无序；允许 null key/values；单线程性能最快
 * - LinkedHashMap：非线程安全；维持插入顺序；允许 null；性能略慢于 HashMap
 * - TreeMap：非线程安全；按 key 有序（默认升序或自定义 Comparator）；不允许 null key；性能最慢（基于红黑树）
 * - Hashtable：线程安全；不允许 null key/values；整体性能最慢（方法级同步）
 */
public class MapPerformanceDemo {

    private static final int PUT_COUNT = 200_000;
    private static final int GET_COUNT = 300_000;

    private static final int CONC_THREADS = 8;
    private static final int CONC_PUTS_PER_THREAD = 25_000; // 总计 200_000 条

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Map 性能与线程安全演示 ===\n");
        System.out.println("单线程基准规模：put=" + PUT_COUNT + ", get=" + GET_COUNT);
        System.out.println("并发写入规模：threads=" + CONC_THREADS + ", perThreadPuts=" + CONC_PUTS_PER_THREAD);
        System.out.println();

        // 1) 单线程性能
        benchmark("HashMap", new HashMap<>());
        benchmark("LinkedHashMap", new LinkedHashMap<>());
        benchmark("TreeMap", new TreeMap<>());
        benchmark("Hashtable", new Hashtable<>());

        System.out.println();

        // 2) 并发写入安全性
        concurrencyCheck("HashMap", new HashMap<>());
        concurrencyCheck("LinkedHashMap", new LinkedHashMap<>());
        concurrencyCheck("TreeMap", new TreeMap<>());
        concurrencyCheck("Hashtable", new Hashtable<>());

        System.out.println("\n=== 说明 ===");
        System.out.println("- HashMap/LinkedHashMap/TreeMap 为非线程安全，多个线程同时写入可能导致数据丢失或状态不一致");
        System.out.println("- Hashtable 为线程安全（方法级 synchronized），但性能较差");
        System.out.println("- 如果需要高并发线程安全 Map，推荐使用 ConcurrentHashMap（此处未演示）");
    }

    private static void benchmark(String name, Map<Integer, Integer> map) {
        System.out.println("— 单线程基准 — " + name);

        // put 基准
        long t1 = System.nanoTime();
        for (int i = 0; i < PUT_COUNT; i++) {
            map.put(i, i);
        }
        long t2 = System.nanoTime();

        // get 基准（命中 + 未命中混合）
        long sum = 0;
        for (int i = 0; i < GET_COUNT; i++) {
            Integer v = map.get(i % PUT_COUNT);
            if (v != null) {
                sum += v;
            }
        }
        long t3 = System.nanoTime();

        System.out.printf("put: %.2f ms, get: %.2f ms, size=%d, checksum=%d%n",
                (t2 - t1) / 1_000_000.0,
                (t3 - t2) / 1_000_000.0,
                map.size(),
                sum);
    }

    private static void concurrencyCheck(String name, Map<Integer, Integer> map) throws InterruptedException {
        System.out.println("\n— 并发写入安全性 — " + name);

        ExecutorService pool = Executors.newFixedThreadPool(CONC_THREADS);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(CONC_THREADS);

        final int expected = CONC_THREADS * CONC_PUTS_PER_THREAD;
        final int namespace = 1_000_000; // 避免与单线程基准数据键冲突

        final List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());

        for (int t = 0; t < CONC_THREADS; t++) {
            final int threadId = t;
            pool.submit(() -> {
                try {
                    start.await();
                    int base = namespace + threadId * CONC_PUTS_PER_THREAD;
                    for (int i = 0; i < CONC_PUTS_PER_THREAD; i++) {
                        // 使用唯一 key，避免覆盖，同步风险来自底层结构非线程安全
                        map.put(base + i, i);
                    }
                } catch (Throwable e) {
                    errors.add(e);
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        done.await(20, TimeUnit.SECONDS);
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        boolean hasError = !errors.isEmpty();
        int size = map.size();
        boolean correct = size >= expected; // 非线程安全可能小于 expected

        System.out.println("期望条数(expected): " + expected);
        System.out.println("实际条数(size):     " + size);
        if (hasError) {
            System.out.println("并发过程中出现异常（可能的线程安全问题，如 ConcurrentModificationException 等）：");
            for (Throwable e : errors) {
                System.out.println(" - " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
        System.out.println("线程安全判断（仅供参考）: " + (correct && !hasError));
        System.out.println("说明: 非线程安全的 Map 在高并发写入时可能出现最终 size 小于期望或抛异常；Hashtable 由于同步通常能达到期望值。");
    }
}
