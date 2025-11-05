package fun.jevon.error;

import java.util.ArrayList;
import java.util.List;

/**
 * OutOfMemoryError – 内存溢出错误
 *
 * 名字由来：
 * Out of Memory（内存溢出） + Error（错误）。
 * 表示 JVM 内存不足，无法分配新的对象。
 *
 * 说明：
 * - Error：系统层次、JVM层次的错误，因此是不可捕捉处理
 * - OutOfMemoryError：当 JVM 无法分配足够的内存来满足对象创建需求时抛出
 * - 通常发生在创建大量对象或数组时，堆内存耗尽
 */
public class OutOfMemoryErrorDemo {

    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        System.out.printf("最大堆内存: %.2f MB%n", rt.maxMemory() / 1024.0 / 1024);
        System.out.printf("初始堆内存: %.2f MB%n", rt.totalMemory() / 1024.0 / 1024);
        System.out.printf("可用空闲内存: %.2f MB%n", rt.freeMemory() / 1024.0 / 1024);
        System.out.println("=== OutOfMemoryError 演示 ===\n");
        System.out.println("尝试创建大量对象导致内存溢出：");
        
        try {
            List<byte[]> list = new ArrayList<>();
            int count = 0;
            while (true) {
                list.add(new byte[10 * 1024 * 1024]); // 每次分配 10MB
                count++;
                if (count % 10 == 0) {
                    long used = rt.totalMemory() - rt.freeMemory();
                    System.out.printf("第 %d 次分配，已用内存：%.2f MB%n", count, used / 1024.0 / 1024);
                }
            }
        } catch (OutOfMemoryError e) {
            System.out.println("捕获到错误：" + e.getClass().getSimpleName());
            // Java heap space
            System.out.println("错误消息：" + e.getMessage());
            System.out.println("\n解释：JVM 无法分配足够的内存，抛出 OutOfMemoryError。");
        }
    }
}

