package fun.jevon.exception;

/**
 * IllegalArgumentException – 非法参数异常
 *
 * 名字由来：
 * Illegal（不合法的） + Argument（参数） + Exception。
 * 表示方法接收到的参数不符合要求。
 */
public class IllegalArgumentExceptionDemo {

    public static void main(String[] args) {
        System.out.println("=== IllegalArgumentException 演示 ===\n");
        System.out.println("设置不合法的线程优先级：");
        
        Thread t = new Thread();
        t.setPriority(100); // 优先级应在1~10之间，会抛出 IllegalArgumentException
        
        System.out.println("解释：方法接收到不合法的参数时，会抛出 IllegalArgumentException。");
    }
}


