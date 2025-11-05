package fun.jevon.exception;

/**
 * NegativeArraySizeException – 负数组大小异常
 *
 * 名字由来：
 * Negative（负数） + Array Size（数组大小） + Exception。
 * 表示创建数组时，给了负数大小。
 */
public class NegativeArraySizeExceptionDemo {

    public static void main(String[] args) {
        System.out.println("=== NegativeArraySizeException 演示 ===\n");
        System.out.println("使用负数创建数组：");
        
        int[] arr = new int[-5]; // 负数数组大小，会抛出 NegativeArraySizeException
        
        System.out.println("解释：创建数组时使用负数大小，会抛出 NegativeArraySizeException。");
    }
}


