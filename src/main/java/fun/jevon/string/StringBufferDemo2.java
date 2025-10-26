package fun.jevon.string;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 性能测试
public class StringBufferDemo2 {
    public static void main(String[] args) {
        System.out.println("=== 性能测试 ===\n");

        int count = 30000;

        // String测试
        long start = System.currentTimeMillis();
        String str = "";
        for (int i = 0; i < count; i++) {
            str += "test" + i;
        }
        long stringTime = System.currentTimeMillis() - start;

        // StringBuffer测试
        start = System.currentTimeMillis();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < count; i++) {
            stringBuffer.append("test").append(i);
        }
        long stringBufferTime = System.currentTimeMillis() - start;

        // StringBuilder测试
        start = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            stringBuilder.append("test").append(i);
        }
        long stringBuilderTime = System.currentTimeMillis() - start;

        System.out.println("String耗时: " + stringTime + "ms");
        System.out.println("StringBuffer耗时: " + stringBufferTime + "ms");
        System.out.println("StringBuilder耗时: " + stringBuilderTime + "ms");
        System.out.println();
        System.out.println("StringBuilder最快，String最慢");
    }
}