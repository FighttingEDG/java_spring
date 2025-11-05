package fun.jevon.exception;

/**
 * ArrayStoreException – 数组存储异常
 *
 * 名字由来：
 * Array（数组） + Store（存放） + Exception。
 * 表示往数组中存放了不兼容类型的对象。
 */
public class ArrayStoreExceptionDemo {

    public static void main(String[] args) {
        System.out.println("=== ArrayStoreException 演示 ===\n");
        System.out.println("往String数组存放int类型：");
        
        try {
            Object[] arr = new String[3];
            arr[0] = 42; // 尝试往String数组放int，会抛出 ArrayStoreException
        } catch (ArrayStoreException e) {
            System.out.println("捕获到异常：" + e.getClass().getSimpleName());
            System.out.println("错误消息：" + e.getMessage());
            System.out.println("\n注意：错误消息只提到了 Integer 类型，");
            System.out.println("     但没有明确说明数组期望的是 String 类型。");
            System.out.println("     这是因为 ArrayStoreException 的消息设计就是这样的：");
            System.out.println("     它只告诉你什么类型不能存储，不告诉你数组期望什么类型。");
        }
        
        System.out.println("\n解释：往数组中存放不兼容类型的对象时，会抛出 ArrayStoreException。");
    }
}


