package fun.jevon.string;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * String、StringBuffer、StringBuilder性能测试演示
 * 
 * 本类主要演示三种字符串操作方式的性能差异：
 * 1. String：不可变字符串，每次拼接都创建新对象，性能最差
 * 2. StringBuffer：可变字符串，线程安全，性能中等
 * 3. StringBuilder：可变字符串，非线程安全，性能最好
 * 
 * 测试场景：对100000次字符串拼接操作进行性能测试
 * 预期结果：StringBuilder > StringBuffer > String（性能从高到低）
 */
public class StringBufferDemo2 {
    public static void main(String[] args) {
        System.out.println("=== 性能测试 ===\n");
        System.out.println("测试场景：对100000次字符串拼接操作进行性能测试\n");

        // 设置测试次数，次数越大，性能差异越明显
        int count = 100000;

        // ========== String测试（性能最差） ==========
        System.out.println("1. String测试（不可变字符串）：");
        // 返回当前的毫秒数
        long start = System.currentTimeMillis();
        String str = "";
        // 使用String进行字符串拼接
        // 每次拼接都会创建新的String对象，导致大量内存分配和垃圾回收
        for (int i = 0; i < count; i++) {
            str += "test" + i;  // 等价于 str = str + "test" + i
        }
        // string操作的耗时
        long stringTime = System.currentTimeMillis() - start;
        System.out.println("String耗时: " + stringTime + "ms");
        System.out.println("说明：String不可变，每次拼接都创建新对象，性能最差\n");

        // ========== StringBuffer测试（性能中等） ==========
        System.out.println("2. StringBuffer测试（可变字符串，线程安全）：");
        start = System.currentTimeMillis();
        StringBuffer stringBuffer = new StringBuffer();
        // 使用StringBuffer进行字符串拼接
        // StringBuffer内部使用char[]数组，可以动态扩容，避免频繁创建对象
        // 所有方法都是synchronized的，保证线程安全，但会有同步开销
        // for是单线程执行的，所以只有跳出之后才会执行后面的代码
        for (int i = 0; i < count; i++) {
            stringBuffer.append("test").append(i);
        }
        long stringBufferTime = System.currentTimeMillis() - start;
        System.out.println("StringBuffer耗时: " + stringBufferTime + "ms");
        System.out.println("说明：StringBuffer可变，线程安全，性能中等\n");

        // ========== StringBuilder测试（性能最好） ==========
        System.out.println("3. StringBuilder测试（可变字符串，非线程安全）：");
        start = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder();
        // 使用StringBuilder进行字符串拼接
        // StringBuilder与StringBuffer类似，但方法不是synchronized的
        // 没有同步开销，性能最好，但非线程安全
        for (int i = 0; i < count; i++) {
            stringBuilder.append("test").append(i);
        }
        long stringBuilderTime = System.currentTimeMillis() - start;
        System.out.println("StringBuilder耗时: " + stringBuilderTime + "ms");
        System.out.println("说明：StringBuilder可变，非线程安全，性能最好\n");

        // ========== 性能对比分析 ==========
        System.out.println("=== 性能对比分析 ===");
        System.out.println("String耗时: " + stringTime + "ms");
        System.out.println("StringBuffer耗时: " + stringBufferTime + "ms");
        System.out.println("StringBuilder耗时: " + stringBuilderTime + "ms");
        System.out.println();
        
        // 计算性能倍数
        if (stringBufferTime > 0) {
            double stringVsBuffer = (double) stringTime / stringBufferTime;
            System.out.println("String比StringBuffer慢 " + String.format("%.2f", stringVsBuffer) + " 倍");
        }
        
        if (stringBuilderTime > 0) {
            double stringVsBuilder = (double) stringTime / stringBuilderTime;
            System.out.println("String比StringBuilder慢 " + String.format("%.2f", stringVsBuilder) + " 倍");
        }
        
        if (stringBuilderTime > 0) {
            double bufferVsBuilder = (double) stringBufferTime / stringBuilderTime;
            System.out.println("StringBuffer比StringBuilder慢 " + String.format("%.2f", bufferVsBuilder) + " 倍");
        }
        
        System.out.println();
        System.out.println("=== 总结 ===");
        System.out.println("性能排序：StringBuilder > StringBuffer > String");
        System.out.println();
        System.out.println("• String（不可变）：");
        System.out.println("  - 优点：线程安全，使用简单");
        System.out.println("  - 缺点：性能最差，频繁创建对象");
        System.out.println("  - 适用：少量字符串操作");
        System.out.println();
        System.out.println("• StringBuffer（可变，线程安全）：");
        System.out.println("  - 优点：线程安全，性能中等");
        System.out.println("  - 缺点：有同步开销");
        System.out.println("  - 适用：多线程环境下的字符串操作");
        System.out.println();
        System.out.println("• StringBuilder（可变，非线程安全）：");
        System.out.println("  - 优点：性能最好，无同步开销");
        System.out.println("  - 缺点：非线程安全");
        System.out.println("  - 适用：单线程环境下的字符串操作");
        System.out.println();
        System.out.println("选择建议：");
        System.out.println("1. 单线程环境：优先使用StringBuilder");
        System.out.println("2. 多线程环境：使用StringBuffer");
        System.out.println("3. 少量操作：可以使用String");
        System.out.println("4. 大量字符串拼接：避免使用String");
    }
}