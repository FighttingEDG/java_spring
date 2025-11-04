package fun.jevon.threadSafety;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 不可变性（Immutability）最简单演示
 *
 * 要点：
 * 1. 不可变对象创建后状态不再改变（字段用 final，且不提供修改方法）。
 * 2. 多线程并发访问同一个不可变对象无需同步，天然线程安全。
 */
public class ImmutabilityDemo {

    /**
     * 一个简单的不可变点对象：x、y 为 final，且无任何修改其状态的方法。
     */
    static final class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        /**
         * 返回一个新的点实例，原对象不变（体现不可变性）。
         */
        public Point move(int dx, int dy) {
            return new Point(this.x + dx, this.y + dy);
        }

        @Override
        public String toString() {
            return "Point{" + "x=" + x + ", y=" + y + '}';
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 不可变性（Immutability）最简单演示 ===\n");

        // 共享的不可变对象
        Point origin = new Point(0, 0);
        System.out.println("初始共享对象: " + origin);

        // 5 个线程并发读取同一个不可变对象，并基于它创建新对象（不影响原对象）
        ExecutorService pool = Executors.newFixedThreadPool(5);
        // 像线程池提交五次任务，具体在哪个线程执行不知道
        for (int i = 1; i <= 5; i++) {
            final int threadId = i;
            // 将任务提交到线程池执行
            pool.submit(() -> {
                // 并发读取无需同步
                System.out.println("线程-" + threadId + " 读取: " + origin);
                // 基于不可变对象创建一个新对象（模拟‘修改’），原对象不变
                Point moved = origin.move(threadId, threadId * 2);
                System.out.println("线程-" + threadId + " 基于共享对象派生的新对象: " + moved);
            });
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        // 验证原对象仍未改变
        System.out.println("\n并发访问后，原共享对象仍为: " + origin);
        System.out.println("说明：不可变对象创建后状态不可变，多线程访问无需额外同步，天然线程安全。");
    }
}


