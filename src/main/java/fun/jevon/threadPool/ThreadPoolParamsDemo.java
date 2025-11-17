package fun.jevon.threadPool;

import java.util.concurrent.*;

/**
 * ThreadPoolExecutor 核心参数演示：
 * 1. corePoolSize
 * 2. maximumPoolSize
 * 3. keepAliveTime + unit
 * 4. workQueue
 * 5. threadFactory
 * 6. handler (拒绝策略)
 *
 * 使用自定义 ThreadPoolExecutor 打印线程创建、任务提交、拒绝策略触发情况。
 */
public class ThreadPoolParamsDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ThreadPoolExecutor 核心参数演示 ===\n");

        int corePoolSize = 2;// 核心线程数量（一直存活）
        int maximumPoolSize = 4;// 能创建的最大线程数（核心 + 非核心）。
        long keepAliveTime = 2L;// 非核心线程空闲多久被回收。
        TimeUnit unit = TimeUnit.SECONDS;// keepAliveTime 的时间单位
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2); // 有界队列，容量为2 // 任务的排队策略：有序array。

        ThreadFactory threadFactory = new ThreadFactory() { // 线程工厂，统一创建线程，常用于设置线程名
            private int index = 1;

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("DemoPool-" + index++);
                System.out.println("  [ThreadFactory] 创建线程：" + t.getName());
                return t;
            }
        };

        RejectedExecutionHandler handler = (r, executor) -> { // 拒绝策略，线程池满时任务的处理方式
            System.out.println("  [RejectedHandler] 任务被拒绝：" + r);
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );

        System.out.println("参数说明：");
        System.out.println("  corePoolSize = " + corePoolSize + "核心线程数");
        System.out.println("  maximumPoolSize = " + maximumPoolSize + "最大可创建线程数量");
        System.out.println("  keepAliveTime = " + keepAliveTime + " " + unit + "非核心线程空闲被回收时间");
        System.out.println("  workQueue = ArrayBlockingQueue(2)");
        System.out.println("  handler = 自定义拒绝策略（打印信息）");
        System.out.println();

        // 提交任务数量 > maximumPoolSize + queue，触发拒绝策略
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            Runnable task = () -> {
                System.out.println(Thread.currentThread().getName() + " 执行任务 #" + taskId);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName() + " 完成任务 #" + taskId);
            };

            System.out.println("提交任务 #" + taskId);
            // 线程池对象调用提交任务的方法
            executor.execute(task);
        }

        // 观测 keepAliveTime 对非核心线程的影响
        System.out.println("\n等待所有任务完成...");
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("线程池已关闭");
    }
}


