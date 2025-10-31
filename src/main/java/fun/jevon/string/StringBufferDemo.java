package fun.jevon.string;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * StringBuffer线程安全测试演示
 * 
 * 本类主要演示StringBuffer和StringBuilder在线程安全方面的差异：
 * 1. StringBuilder：非线程安全，多线程环境下可能出现数据丢失
 * 2. StringBuffer：线程安全，使用synchronized关键字保证线程安全
 * 
 * 测试场景：10个线程同时向同一个StringBuilder/StringBuffer对象追加数据
 * 预期结果：StringBuilder可能出现数据丢失，StringBuffer保证数据完整
 */
public class StringBufferDemo {
    
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("=== 线程安全测试 ===\n");
        
        // ========== StringBuilder测试（非线程安全） ==========
        System.out.println("1. StringBuilder测试（非线程安全）：");
        StringBuilder sb = new StringBuilder();
        // 创建线程池，10个线程（自定义线程名，便于在 VisualVM 中识别）
        ExecutorService executor = Executors.newFixedThreadPool(10, r -> {
            Thread t = new Thread(r);
            t.setName("SB-Builder-" + t.getId());
            return t;
        });
        
        // 启动10个线程，每个线程向StringBuilder追加100000000次数据
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                // 每个线程追加100000000次，每次追加固定长度的字符串："A-0," 这样的格式
                for (int j = 0; j < 100000000; j++) {
                    sb.append("A-" + j + ",");
                }
            });
        }
        
        // 不再接收新任务，现有任务都执行完再关闭线程池
        executor.shutdown();
        // 等待线程池结束（最多1分钟），避免主动sleep轮询
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        // 计算预期长度：10个线程 × 100000000次 × 固定长度
        // 字符串格式："A-" + j + ","，长度分析：
        // A-0,=4, A-1,=4, ..., A-9,=4, A-10,=5, ..., A-99,=5, A-100,=6, ..., A-999,=6
        int expectedLength = 0;
        for (int j = 0; j < 100000000; j++) {
            String testStr = "A-" + j + ",";
            expectedLength += testStr.length();
        }
        expectedLength *= 10; // 10个线程
        System.out.println("StringBuilder预期长度: " + expectedLength);
        System.out.println("StringBuilder实际长度: " + sb.length());
        System.out.println("数据丢失: " + (sb.length() < expectedLength));
        System.out.println("说明：StringBuilder非线程安全，多线程操作可能导致数据丢失\n");
        
        // ========== StringBuffer测试（线程安全） ==========
        System.out.println("2. StringBuffer测试（线程安全）：");
        // 直接使用StringBuffer，而不是通过StringBufferDemo包装
        StringBuffer stringBuffer = new StringBuffer();
        // 重新创建线程池（自定义线程名，便于在 VisualVM 中识别）
        executor = Executors.newFixedThreadPool(10, r -> {
            Thread t = new Thread(r);
            t.setName("SB-Buffer-" + t.getId());
            return t;
        });
        // 启动10个线程，每个线程向StringBuffer追加100000000次数据
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                // 每个线程追加100000000次，每次追加固定长度的字符串："A-" + j + ","
                for (int j = 0; j < 100000000; j++) {
                    stringBuffer.append("A-" + j + ",");
                }
            });
        }
        
        // 等待所有线程执行完毕
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        // 使用相同的预期长度计算
        System.out.println("StringBuffer预期长度: " + expectedLength);
        System.out.println("StringBuffer实际长度: " + stringBuffer.length());
        System.out.println("数据完整: " + (stringBuffer.length() == expectedLength));
        System.out.println("说明：StringBuffer线程安全，多线程操作保证数据完整");
        
        // ========== 总结 ==========
        System.out.println("\n=== 总结 ===");
        System.out.println("• StringBuilder：非线程安全，性能更好，适合单线程环境");
        System.out.println("• StringBuffer：线程安全，性能稍差，适合多线程环境");
        System.out.println("• 选择建议：");
        System.out.println("  - 单线程环境：优先使用StringBuilder");
        System.out.println("  - 多线程环境：必须使用StringBuffer");

        // 可选：任务结束后保活，便于 VisualVM 连接与观察
        if (Boolean.getBoolean("hold.after")) {
            System.out.println("\n[hold.after=true] 程序已完成计算，按 Enter 退出...");
            System.in.read();
        }
    }
}