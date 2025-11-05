package fun.jevon.exception;

import java.sql.*;

/**
 * SQLException – 数据库异常
 *
 * 名字由来：
 * SQL 是结构化查询语言 (Structured Query Language)，用于数据库操作。
 * 因此 SQLException 表示与数据库访问或 SQL 执行相关的错误。
 */
public class SQLExceptionDemo {

    public static void main(String[] args) throws SQLException {
        System.out.println("=== SQLException 演示 ===\n");
        System.out.println("尝试连接数据库（使用错误的连接信息）：");
        
        // 连接数据库失败，会抛出 SQLException
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db", "root", "wrong");
        
        System.out.println("解释：连接数据库失败，会抛出 SQLException。");
    }
}


