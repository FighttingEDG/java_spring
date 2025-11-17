package fun.jevon.threadPool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Java 内置六种线程池类型演示：
 * 1. newCachedThreadPool
 * 2. newFixedThreadPool
 * 3. newSingleThreadExecutor
 * 4. newSingleThreadScheduledExecutor
 * 5. newScheduledThreadPool
 * 6. newWorkStealingPool
 *
 * 每种线程池提交简单任务，说明其适用场景。
 */
public class ThreadPoolTypesDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 线程池类型演示 ===\n");

        demoCachedThreadPool();
        divider();

        demoFixedThreadPool();
        divider();

        demoSingleThreadExecutor();
        divider();

        demoSingleThreadScheduledExecutor();
        divider();

        demoScheduledThreadPool();
        divider();

        demoWorkStealingPool();
    }

    private static void demoCachedThreadPool() throws InterruptedException {
        System.out.println("1) newCachedThreadPool — 缓存型线程池，线程可复用，空闲60s销毁");
        ExecutorService pool = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setName("Cached-" + t.getId());
            return t;
        });
        runSimpleTasks(pool, "Cached");
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void demoFixedThreadPool() throws InterruptedException {
        System.out.println("2) newFixedThreadPool — 固定线程数，适合稳定负载");
        ExecutorService pool = Executors.newFixedThreadPool(3, r -> {
            Thread t = new Thread(r);
            t.setName("Fixed-" + t.getId());
            return t;
        });
        runSimpleTasks(pool, "Fixed");
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void demoSingleThreadExecutor() throws InterruptedException {
        System.out.println("3) newSingleThreadExecutor — 单线程执行器，任务严格串行");
        ExecutorService pool = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r);
            t.setName("Single-" + t.getId());
            return t;
        });
        runSimpleTasks(pool, "Single");
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void demoSingleThreadScheduledExecutor() throws InterruptedException {
        System.out.println("4) newSingleThreadScheduledExecutor — 单线程定时任务线程池");
        ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setName("SingleScheduled-" + t.getId());
            return t;
        });
        pool.schedule(() -> System.out.println(Thread.currentThread().getName() + " 延迟任务执行"), 500, TimeUnit.MILLISECONDS);
        pool.scheduleAtFixedRate(() -> System.out.println(Thread.currentThread().getName() + " 固定速率任务"), 0, 1, TimeUnit.SECONDS);
        Thread.sleep(2200);
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void demoScheduledThreadPool() throws InterruptedException {
        System.out.println("5) newScheduledThreadPool — 多线程定时任务线程池，支持延迟与周期执行");
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r);
            t.setName("Scheduled-" + t.getId());
            return t;
        });
        pool.schedule(() -> System.out.println(Thread.currentThread().getName() + " 延迟任务"), 300, TimeUnit.MILLISECONDS);
        pool.scheduleAtFixedRate(() -> System.out.println(Thread.currentThread().getName() + " 周期任务"), 0, 700, TimeUnit.MILLISECONDS);
        Thread.sleep(2000);
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void demoWorkStealingPool() throws InterruptedException {
        System.out.println("6) newWorkStealingPool — 基于 ForkJoinPool 的任务窃取线程池，线程数=CPU核心");
        ExecutorService pool = Executors.newWorkStealingPool();
        List<Callable<String>> tasks = Arrays.asList(
                sleepingTask("A", 500),
                sleepingTask("B", 800),
                sleepingTask("C", 300),
                sleepingTask("D", 600)
        );
        pool.invokeAll(tasks).forEach(f -> {
            try {
                System.out.println("  结果：" + f.get());
            } catch (InterruptedException | ExecutionException ignored) {
            }
        });
        // newWorkStealingPool 使用守护线程，无法显式 shutdown，这里等待任务完成
        Thread.sleep(1000);
    }

    private static Callable<String> sleepingTask(String name, long ms) {
        return () -> {
            String thread = Thread.currentThread().getName();
            System.out.println("  任务" + name + " 在 " + thread + " 中执行");
            Thread.sleep(ms);
            return "任务" + name + " 完成";
        };
    }

    private static void runSimpleTasks(ExecutorService pool, String prefix) {
        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            pool.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " 执行任务 " + prefix + "-" + taskId);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName() + " 完成任务 " + prefix + "-" + taskId);
            });
        }
    }

    private static void divider() {
        System.out.println("\n" + "-".repeat(60) + "\n");
    }
}


