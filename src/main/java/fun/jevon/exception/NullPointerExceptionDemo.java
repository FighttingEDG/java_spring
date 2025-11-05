package fun.jevon.exception;

/**
 * NullPointerException – 空指针异常
 *
 * 名字由来：
 * Null（空值） + Pointer（指针） + Exception（异常）。
 * 意思是"你试图通过一个空引用访问对象的内容"。
 */
public class NullPointerExceptionDemo {

    public static void main(String[] args) {
        System.out.println("=== NullPointerException 演示 ===\n");
        System.out.println("对空对象调用方法：");
        
        String s = null;
        s.length(); // 对空对象调用方法，会抛出 NullPointerException
        
        System.out.println("解释：试图通过空引用访问对象的方法时，会抛出 NullPointerException。");
    }
}


