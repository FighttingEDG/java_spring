package fun.jevon.exception;

/**
 * SecurityException – 安全异常
 *
 * 名字由来：
 * Security（安全） + Exception。
 * 表示安全管理器检测到非法操作。
 * 
 * 注意：System.setSecurityManager() 在 Java 17+ 中已被弃用，在 Java 21+ 中已移除。
 * 现代 Java 应用中很少会遇到 SecurityException，除非在受限环境（如 Applet、受限容器）中运行。
 */
public class SecurityExceptionDemo {

    public static void main(String[] args) {
        System.out.println("=== SecurityException 演示 ===\n");
        
        // 方式1：手动抛出 SecurityException（用于演示）
        System.out.println("方式1：手动抛出 SecurityException（演示用）：");
        try {
            throw new SecurityException("模拟安全管理器检测到非法操作");
        } catch (SecurityException e) {
            System.out.println("捕获到异常：" + e.getClass().getSimpleName());
            System.out.println("错误消息：" + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // 方式2：尝试访问受限制的系统属性（在某些环境中可能触发）
        System.out.println("方式2：尝试访问系统属性（在受限环境中可能触发 SecurityException）：");
        try {
            // 在某些安全管理器配置下，访问系统属性可能被禁止
            String javaHome = System.getProperty("java.home");
            System.out.println("成功访问 java.home: " + javaHome);
            System.out.println("注意：在普通应用中，访问系统属性通常不会被阻止。");
        } catch (SecurityException e) {
            System.out.println("捕获到异常：" + e.getClass().getSimpleName());
            System.out.println("错误消息：" + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        System.out.println("说明：");
        System.out.println("1. System.setSecurityManager() 在 Java 17+ 中已被弃用，Java 21+ 中已移除");
        System.out.println("2. 现代 Java 应用中很少会遇到 SecurityException");
        System.out.println("3. SecurityException 主要用于：");
        System.out.println("   - 旧版 Java Applet 环境");
        System.out.println("   - 受限的容器环境");
        System.out.println("   - 自定义安全管理器配置的环境");
        System.out.println("4. 在普通 Java 应用中，通常不会触发此异常");
    }
}


