package fun.jevon.exception;

/**
 * ClassCastException – 类型强制转换异常
 *
 * 名字由来：
 * Class（类） + Cast（强制转换） + Exception。
 * 代表"类类型转换出错"。
 */
public class ClassCastExceptionDemo {

    public static void main(String[] args) {
        System.out.println("=== ClassCastException 演示 ===\n");
        System.out.println("错误的类型转换：");
        
        Object x = "Hello";
        Integer y = (Integer) x; // 错误的类型转换，会抛出 ClassCastException
        // 其实这里抛出错误就不会再运行了，中断了
        System.out.println(y);
        System.out.println("解释：尝试将对象转换为不兼容的类型时，会抛出 ClassCastException。");
    }
}


