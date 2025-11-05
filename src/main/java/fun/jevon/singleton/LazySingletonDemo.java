package fun.jevon.singleton;

/**
 * （3）懒汉式单例模式（Lazy Singleton）最简单演示
 *
 * 特点：
 * - 延迟加载：只有在第一次调用 getInstance() 时才创建实例
 * - 使用 synchronized 方法保证线程安全
 * - 每次访问都要加锁，性能较低
 */
public class LazySingletonDemo {

    /**
     * 懒汉式单例：延迟加载，使用 synchronized 保证线程安全
     */
    static class LazySingleton {
        // 私有静态变量，初始为 null
        // 饿汉是直接new自己类
        // 懒汉是调用的时候才会new
        private static LazySingleton instance;

        // 私有构造函数，防止外部创建实例
        private LazySingleton() {
            System.out.println("懒汉式单例：实例已创建（第一次调用时）");
        }

        // 公共静态方法，使用 synchronized 保证线程安全
        // 缺点：每次调用都要加锁，性能较差
        public static synchronized LazySingleton getInstance() {
            if (instance == null) {
                instance = new LazySingleton();
            }
            return instance;
        }

        public void doSomething() {
            System.out.println("懒汉式单例：执行操作");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 懒汉式单例模式演示 ===\n");

        // 获取实例（第一次调用时才会创建）
        LazySingleton singleton1 = LazySingleton.getInstance();
        // 第二次是拿到第一次创建后对象的引用
        LazySingleton singleton2 = LazySingleton.getInstance();

        // 验证是同一个实例
        System.out.println("singleton1 == singleton2: " + (singleton1 == singleton2));
        System.out.println("singleton1.hashCode(): " + singleton1.hashCode());
        System.out.println("singleton2.hashCode(): " + singleton2.hashCode());

        singleton1.doSomething();
    }
}

