package fun.jevon.string;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 线程安全测试
public class StringBufferDemo {
    private StringBuffer stringBuffer = new StringBuffer();
    
    public void append(String str) {
        stringBuffer.append(str);
    }
    
    public int length() {
        return stringBuffer.length();
    }
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 线程安全测试 ===\n");
        
        // StringBuilder测试（非线程安全）
        StringBuilder sb = new StringBuilder();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    sb.append("T" + threadId + "-" + j + ",");
                }
            });
        }
        
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(10);
        }
        
        System.out.println("StringBuilder预期长度: " + (10 * 1000 * 8));
        System.out.println("StringBuilder实际长度: " + sb.length());
        System.out.println("数据丢失: " + (sb.length() < 10 * 1000 * 8));
        System.out.println();
        
        // StringBuffer测试（线程安全）
        StringBufferDemo stringBufferDemo = new StringBufferDemo();
        executor = Executors.newFixedThreadPool(10);
        
        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            executor.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    stringBufferDemo.append("T" + threadId + "-" + j + ",");
                }
            });
        }
        
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(10);
        }
        
        System.out.println("StringBuffer预期长度: " + (10 * 1000 * 8));
        System.out.println("StringBuffer实际长度: " + stringBufferDemo.length());
        System.out.println("数据完整: " + (stringBufferDemo.length() == 10 * 1000 * 8));
    }
}