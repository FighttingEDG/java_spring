package fun.jevon.error;

/**
 * NoClassDefFoundError – 类加载错误
 *
 * 名字由来：
 * No（没有） + Class（类） + Def（定义） + Found（找到） + Error（错误）。
 * 表示找不到类的定义，类定义未找到错误。
 *
 * 说明：
 * - Error：系统层次、JVM层次的错误，因此是不可捕捉处理
 * - NoClassDefFoundError：当 JVM 在运行时找不到类的定义时抛出
 * - 通常发生在编译时存在但运行时缺失的类
 *
 * 注意：
 * - 这个错误在实际演示中较难直接触发（需要特定的类路径配置）
 * - 这里展示一个简单的示例说明
 */
public class NoClassDefFoundErrorDemo {

    /**
     * 尝试加载一个不存在的类
     * 注意：这种方式通常会抛出 ClassNotFoundException，而不是 NoClassDefFoundError
     * NoClassDefFoundError 通常发生在：
     * 1. 编译时存在，但运行时类文件缺失
     * 2. 类初始化失败后再次尝试加载
     */
    public static void main(String[] args) {
        System.out.println("=== NoClassDefFoundError 演示 ===\n");
        System.out.println("说明：NoClassDefFoundError 的触发场景：");
        
        System.out.println("\n场景1：编译时存在，运行时类文件缺失");
        System.out.println("  - 编译时类存在于 classpath 中");
        System.out.println("  - 运行时类文件被删除或不在 classpath 中");
        System.out.println("  - 此时会抛出 NoClassDefFoundError");
        
        System.out.println("\n场景2：类初始化失败后再次尝试加载");
        System.out.println("  - 类第一次加载时初始化失败（抛出 Exception）");
        System.out.println("  - 再次尝试加载该类时，会抛出 NoClassDefFoundError");
        
        System.out.println("\n场景3：演示类初始化失败（模拟）");
        try {
            // 尝试加载一个可能不存在的类
            Class<?> clazz = Class.forName("NonExistentClass12345");
        } catch (ClassNotFoundException e) {
            System.out.println("捕获到：" + e.getClass().getSimpleName());
            System.out.println("说明：ClassNotFoundException 是编译时检查到的类不存在");
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("\n解释：");
        System.out.println("- NoClassDefFoundError 是运行时错误，表示类定义找不到");
        System.out.println("- 与 ClassNotFoundException 的区别：");
        System.out.println("  * ClassNotFoundException：编译时就知道类可能不存在（checked exception）");
        System.out.println("  * NoClassDefFoundError：编译时存在，运行时找不到（unchecked error）");
        System.out.println("- 实际项目中，NoClassDefFoundError 通常由以下原因导致：");
        System.out.println("  1. 类路径配置错误");
        System.out.println("  2. 依赖缺失（如 jar 包未正确加载）");
        System.out.println("  3. 类初始化失败后的再次加载");
    }
}

