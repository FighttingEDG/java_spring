package fun.jevon.thread.threadSafety;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 并发容器（Concurrent Collections）最简单演示
 *
 * 要点：
 * 1. ConcurrentHashMap：分段/无锁化方案，支持并发读写；使用 compute/merge 等原子组合操作避免竞态。
 * 2. CopyOnWriteArrayList：写时复制，读无锁，迭代器是快照；适合读多写少场景。
 * 3. ConcurrentLinkedQueue：基于无锁的并发队列，适合多生产者/多消费者，高并发入队/出队。
 */
public class ConcurrentCollectionsDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 并发容器（Concurrent Collections）最简单演示 ===\n");

        // 使用 compute 进行原子读改写
        demoConcurrentHashMap();
        System.out.println();
        // 并发添加与快照迭代
        demoCopyOnWriteArrayList();
        System.out.println();
        // 多生产者单消费者
        demoConcurrentLinkedQueue();
    }
    // 使用 compute 进行原子读改写
    private static void demoConcurrentHashMap() throws InterruptedException {
        System.out.println("1) ConcurrentHashMap 并发计数演示： 使用 compute 进行原子读改写，避免竞态");

        final int threads = 10;
        final int perThreadOps = 50_000;
        final String key = "hits";

        Map<String, Integer> counter = new ConcurrentHashMap<>();

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                for (int j = 0; j < perThreadOps; j++) {
                    counter.compute(key, (k, v) -> v == null ? 1 : v + 1);
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        int expected = threads * perThreadOps;
        int actual = counter.getOrDefault(key, 0);
        System.out.println("预期计数 = " + expected + ", 实际计数 = " + actual + ", 正确性 = " + (expected == actual));
    }

    // 并发添加与快照迭代
    private static void demoCopyOnWriteArrayList() throws InterruptedException {
        System.out.println("2) CopyOnWriteArrayList 并发添加与快照迭代演示： - 写时复制，读无锁；迭代器为快照，遍历过程中并发添加不会抛异常");

        final int threads = 5;
        final int perThreadAdds = 10_000;
        List<Integer> list = new CopyOnWriteArrayList<>();

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            final int base = i * perThreadAdds;
            pool.submit(() -> {
                for (int j = 0; j < perThreadAdds; j++) {
                    list.add(base + j);
                }
            });
        }

        // 同时在另一个线程快照遍历（不会受到并发 add 的影响，也不会抛 ConcurrentModificationException）
        pool.submit(() -> {
            int seen = 0;
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                it.next();
                seen++;
            }
            System.out.println("快照迭代看到的元素数（可能 < 最终大小）= " + seen);
        });

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        int expected = threads * perThreadAdds;
        System.out.println("最终列表大小 = " + list.size() + ", 预期 = " + expected + ", 正确性 = " + (list.size() == expected));
        System.out.println("说明：CopyOnWriteArrayList 适合读多写少；频繁写入会带来复制开销。");
    }

    // 多生产者单消费者
    private static void demoConcurrentLinkedQueue() throws InterruptedException {
        System.out.println("3) ConcurrentLinkedQueue 多生产者单消费者演示：\n- 基于无锁链表的高并发队列，适合多线程并发入队/出队");

        final int producers = 5;
        final int perProducer = 20_000;
        final int expected = producers * perProducer;

        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();

        ExecutorService pool = Executors.newFixedThreadPool(producers + 1);
        // 多生产者并发入队
        for (int i = 0; i < producers; i++) {
            final int base = i * perProducer;
            pool.submit(() -> {
                for (int j = 0; j < perProducer; j++) {
                    queue.offer(base + j);
                }
            });
        }

        // 单消费者持续出队计数
        final int[] consumed = {0};
        pool.submit(() -> {
            int c = 0;
            Integer v;
            // 自旋消费直到达到预期数量
            while (c < expected) {
                v = queue.poll();
                if (v != null) {
                    c++;
                }
            }
            consumed[0] = c;
        });

        pool.shutdown();
        pool.awaitTermination(15, TimeUnit.SECONDS);

        System.out.println("入队总数 = " + expected + ", 实际出队数 = " + consumed[0] + ", 正确性 = " + (consumed[0] == expected));
    }
}


