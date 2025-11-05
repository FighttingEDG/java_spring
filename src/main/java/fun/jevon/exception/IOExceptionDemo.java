package fun.jevon.exception;

import java.io.*;

/**
 * IOException – 输入输出异常
 *
 * 名字由来：
 * IO 是 Input/Output 的缩写，Exception 就是"异常"。
 * 因此这类异常表示在读写数据时出现问题。
 */
public class IOExceptionDemo {

    public static void main(String[] args) throws IOException {
        System.out.println("=== IOException 演示 ===\n");
        System.out.println("尝试读取一个不存在的文件：");
        
        // 文件不存在，会抛出 IOException
        FileReader fr = new FileReader("not_exist.txt");
        
        System.out.println("解释：尝试读取一个不存在的文件时，系统抛出 IOException。");
    }
}


