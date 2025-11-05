package fun.jevon.exception;

import java.util.*;

/**
 * UnsupportedOperationException – 不支持的操作异常
 *
 * 名字由来：
 * Unsupported（不支持的） + Operation（操作） + Exception。
 * 表示方法被调用，但该操作在当前对象中未实现或不被允许。
 */
public class UnsupportedOperationExceptionDemo {

    public static void main(String[] args) {
        System.out.println("=== UnsupportedOperationException 演示 ===\n");
        System.out.println("对固定长度列表执行添加操作：");
        
        List<String> list = Arrays.asList("a", "b", "c");
        list.add("d"); // asList返回的是固定长度列表，会抛出 UnsupportedOperationException
        
        System.out.println("解释：方法被调用但该操作在当前对象中未实现或不被允许时，会抛出 UnsupportedOperationException。");
    }
}


