package fun.jevon.error;

/**
 * StackOverflowError – 栈溢出错误
 *
 * 名字由来：
 * Stack（栈） + Overflow（溢出） + Error（错误）。
 * 表示方法调用栈溢出，超过了 JVM 的栈深度限制。
 *
 * 说明：
 * - Error：系统层次、JVM层次的错误，因此是不可捕捉处理
 * - StackOverflowError：当方法调用栈深度超过 JVM 限制时抛出
 * - 通常发生在无限递归调用时
 */
public class StackOverflowErrorDemo {

    /**
     * 无限递归方法，导致栈溢出
     */
    public static void recursiveMethod(int count) {
        System.out.println("递归调用次数: " + count);
        // 在方法体中再次调用自己，实现递归
        recursiveMethod(count + 1); // 无限递归
    }

    public static void main(String[] args) {
        System.out.println("=== StackOverflowError 演示 ===\n");
        System.out.println("无限递归调用导致栈溢出：");
        
        try {
            recursiveMethod(1);
        } catch (StackOverflowError e) {
            System.out.println("\n捕获到错误：" + e.getClass().getSimpleName());
            System.out.println("错误消息：" + e.getMessage());
            System.out.println("\n解释：方法调用栈深度超过 JVM 限制，抛出 StackOverflowError。");
        }
    }
}

