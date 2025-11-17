package fun.jevon.threadPool;

import java.util.concurrent.*;

/**
 * 线程池任务提交流程演示：
 *
 * 步骤：
 * 1. 任务到来 -> corePoolSize 是否满？
 * 2. 未满：创建核心线程执行
 * 3. 已满：任务入队（workQueue）
 * 4. 队列也满：是否小于 maximumPoolSize？
 *      - 小于：创建非核心线程执行
 *      - 否则：触发拒绝策略
 */
public class ThreadPoolWorkflowDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 线程池任务处理流程演示 ===\n");

        int corePoolSize = 1;
        int maximumPoolSize = 2;
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1); // 小队列，方便观察入队/拒绝

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                5,
                TimeUnit.SECONDS,
                queue,
                new NamedThreadFactory("Workflow"),
                new ThreadPoolExecutor.AbortPolicy()
        );

        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            Runnable task = () -> {
                log("任务#" + taskId + " 在 " + Thread.currentThread().getName() + " 执行中...");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                log("任务#" + taskId + " 在 " + Thread.currentThread().getName() + " 执行完成");
            };

            try {
                log("提交任务#" + taskId);
                executor.execute(task);
            } catch (RejectedExecutionException ex) {
                log("任务#" + taskId + " 被拒绝：" + ex.getClass().getSimpleName());
            }
            Thread.sleep(200); // 间隔提交，观察流程
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        log("线程池已关闭");
    }

    private static void log(String msg) {
        System.out.println("[Workflow] " + msg);
    }

    private static class NamedThreadFactory implements ThreadFactory {
        private final String prefix;
        private int index = 1;

        NamedThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName(prefix + "-Thread-" + index++);
            log("创建线程：" + t.getName());
            return t;
        }
    }
}


