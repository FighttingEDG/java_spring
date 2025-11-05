package fun.jevon.exception;

/**
 * ArithmeticException – 算术运算异常
 *
 * 名字由来：
 * Arithmetic（算术） + Exception。
 * 表示数学运算中出错，比如除以零。
 */
public class ArithmeticExceptionDemo {

    public static void main(String[] args) {
        System.out.println("=== ArithmeticException 演示 ===\n");
        System.out.println("除以零：");
        
        int x = 10 / 0; // 除以0，会抛出 ArithmeticException
        
        System.out.println("解释：进行数学运算时出错（如除以零），会抛出 ArithmeticException。");
    }
}


