package fun.jevon.thread.threadstartAndRun;

/**
 * Demo：start() 和 run() 方法的区别演示
 *
 * 关键区别：
 * 1. 调用 run() 方法：
 *    - 不开启线程，仅是对象调用方法
 *    - 在主线程中执行，是同步的
 *    - 只是普通方法调用
 *
 * 2. 调用 start() 方法：
 *    - 开启新线程，并让 JVM 调用 run() 方法在开启的线程中执行
 *    - 使得线程进入就绪状态，然后由 JVM 调度执行
 *    - 是异步的，新线程和主线程并发执行
 */
public class ThreadStartAndRunDemo {

    static class MyThread extends Thread {
        private String name;

        public MyThread(String name) {
            this.name = "Demo-" + name; // 给线程名字加 Demo 前缀，避免和 JVM 分配的名字冲突
        }

        @Override
        public void run() {
            System.out.println("线程 " + name + " 正在执行，当前线程：" + Thread.currentThread().getName());
            try {
                Thread.sleep(1000); // 模拟任务执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程 " + name + " 执行完成，当前线程：" + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Demo：start() 和 run() 方法的区别演示 ===\n");
        System.out.println("主线程名称：" + Thread.currentThread().getName() + "\n");

        // ========== 方式1：调用 run() 方法 ==========
        System.out.println("【方式1：调用 run() 方法】");
        System.out.println("说明：不开启线程，仅是对象调用方法，在主线程中执行\n");

        MyThread thread1 = new MyThread("Thread-1");
        System.out.println("调用 thread1.run()：");
        thread1.run(); // 直接调用 run() 方法，不开启新线程
        System.out.println("run() 方法调用完成\n");

        // ========== 方式2：调用 start() 方法 ==========
        System.out.println("【方式2：调用 start() 方法】");
        System.out.println("说明：开启新线程，JVM 调用 run() 方法在新线程中执行\n");

        MyThread thread2 = new MyThread("Thread-2");
        System.out.println("调用 thread2.start()：");
        // 确实是新线程，new的是带Demo的，Thread-1是jvm开的线程
        thread2.start(); // 调用 start() 方法，开启新线程
        System.out.println("start() 方法调用完成（注意：此时 run() 可能还在执行中）\n");

        // 等待线程执行完成
        thread2.join();
        System.out.println("线程 Demo-Thread-2 执行完成\n");

        // ========== 对比演示：并发执行 ==========
        System.out.println("=".repeat(50));
        System.out.println("\n【对比演示：并发执行】\n");

        // 同步执行就是串行执行
        System.out.println("演示1：使用 run() 方法（同步执行）：");
        MyThread thread3 = new MyThread("Thread-3");
        MyThread thread4 = new MyThread("Thread-4");
        long startTime1 = System.currentTimeMillis();
        thread3.run(); // 在主线程中执行
        thread4.run(); // 在主线程中执行
        long endTime1 = System.currentTimeMillis();
        System.out.println("总耗时：" + (endTime1 - startTime1) + " 毫秒（串行执行）\n");

        System.out.println("演示2：使用 start() 方法（并发执行）：");
        MyThread thread5 = new MyThread("Thread-5");
        MyThread thread6 = new MyThread("Thread-6");
        long startTime2 = System.currentTimeMillis();
        thread5.start(); // 开启新线程
        thread6.start(); // 开启新线程
        thread5.join(); // 等待线程5完成
        thread6.join(); // 等待线程6完成
        long endTime2 = System.currentTimeMillis();
        System.out.println("总耗时：" + (endTime2 - startTime2) + " 毫秒（并发执行，时间更短）\n");

        // ========== 总结 ==========
        System.out.println("=".repeat(50));
        System.out.println("\n【总结】");
        System.out.println("1. run() 方法：");
        System.out.println("   - 不开启线程，只是普通方法调用");
        System.out.println("   - 在主线程中执行，是同步的");
        System.out.println("   - 多个线程对象调用 run() 是串行执行的");
        System.out.println("\n2. start() 方法：");
        System.out.println("   - 开启新线程，JVM 调用 run() 方法在新线程中执行");
        System.out.println("   - 使得线程进入就绪状态，然后由 JVM 调度执行");
        System.out.println("   - 是异步的，新线程和主线程并发执行");
        System.out.println("   - 多个线程对象调用 start() 是并发执行的");
        System.out.println("\n3. 注意事项：");
        System.out.println("   - 同一个线程对象不能多次调用 start()，会抛出 IllegalThreadStateException");
        System.out.println("   - 要启动新线程，必须调用 start() 方法，而不是 run() 方法");
    }
}
