package fun.jevon.exception;

/**
 * NumberFormatException – 数字格式异常
 *
 * 名字由来：
 * Number Format（数字格式） + Exception。
 * 表示字符串转数字时格式不合法。
 */
public class NumberFormatExceptionDemo {

    public static void main(String[] args) {
        System.out.println("=== NumberFormatException 演示 ===\n");
        System.out.println("将非数字字符串转换为整数：");
        
        int n = Integer.parseInt("abc"); // 不是数字，会抛出 NumberFormatException
        
        System.out.println("解释：字符串转数字时格式不合法，会抛出 NumberFormatException。");
    }
}


