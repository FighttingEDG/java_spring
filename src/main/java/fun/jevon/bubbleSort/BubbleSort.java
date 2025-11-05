package fun.jevon.bubbleSort;


import java.util.ArrayList;
import java.util.Arrays;

// 冒泡排序关键点，第一个for循环控制的是趟数，第二个for循环控制的是需要冒泡的次数
public class BubbleSort {
    // 定义数组
    public static void main(String[] args) {
        int[] arr = {4, 7, 2, 1, 9};
        System.out.println("冒泡前的数组" + Arrays.toString(arr));

        // 冒泡排序
        bubbleSort(arr);

        // 冒泡后的数组
        System.out.println("冒泡排序后的数组：" + Arrays.toString(arr));
    }

    // 需要传一个数组，只有加了static才能在main中使用，因为main也是static，都是静态上下文，才能调用
    public static void bubbleSort(int[] arr) {
        // 外循环控制趟数
        for (int i = 0; i < arr.length - 1; i++) {
        // 内循环控制需要冒泡的次数
            for (int j = 0; j < arr.length - i - 1; j++){
                if ( arr[j] > arr[j+1] ){
                    int temp  = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }
}
