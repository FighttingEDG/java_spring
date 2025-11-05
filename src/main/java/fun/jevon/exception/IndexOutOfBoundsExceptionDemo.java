package fun.jevon.exception;

/**
 * IndexOutOfBoundsException – 下标越界异常
 *
 * 名字由来：
 * Index（索引） + Out of Bounds（超出界限） + Exception。
 * 表示索引访问超出数组或集合范围。
 */
public class IndexOutOfBoundsExceptionDemo {

    public static void main(String[] args) {
        System.out.println("=== IndexOutOfBoundsException 演示 ===\n");
        System.out.println("访问数组越界索引：");
        
        int[] arr = {1, 2, 3};
        System.out.println(arr[3]); // 索引3不存在，会抛出 IndexOutOfBoundsException
        
        System.out.println("解释：索引访问超出数组或集合范围时，会抛出 IndexOutOfBoundsException。");
    }
}


