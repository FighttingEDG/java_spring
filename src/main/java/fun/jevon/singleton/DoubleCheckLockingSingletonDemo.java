package fun.jevon.singleton;

/**
 * （2）双检锁单例模式（Double-Check Locking Singleton）最简单演示
 *
 * 特点：
 * - 延迟加载：只有在第一次调用 getInstance() 时才创建实例
 * - 双重检查：第一次检查避免不必要的加锁，第二次检查保证线程安全
 * - 使用 volatile 保证可见性，synchronized 保证线程安全
 * - 性能介于饿汉式和懒汉式之间
 * - 其实就是实例化的时候，保证是单例的就行了，通过双重检查和加锁实现
 */
public class DoubleCheckLockingSingletonDemo {

    /**
     * 双检锁单例：延迟加载 + 双重检查 + volatile
     */
    static class DoubleCheckLockingSingleton {
        // 使用 volatile 保证可见性，防止指令重排序
        // volatile 在双重检查锁单例中防止“指令重排”，确保不会返回一个未初始化完成的对象
        private static volatile DoubleCheckLockingSingleton instance;

        // 私有构造函数，防止外部创建实例
        private DoubleCheckLockingSingleton() {
            System.out.println("双检锁单例：实例已创建（第一次调用时）");
        }

        // 公共静态方法，双重检查锁定
        public static DoubleCheckLockingSingleton getInstance() {
            // 第一次检查：如果实例已存在，直接返回，避免不必要的加锁
            // 减少加锁次数，提高性能
            if (instance == null) {
                // 加锁，保证只有一个线程能创建实例
                // DoubleCheckLockingSingleton.class是锁对象
                // 多个地方使用synchronized (DoubleCheckLockingSingleton.class) {}就会发生锁竞争
                synchronized (DoubleCheckLockingSingleton.class) {
                    // 第二次检查：防止多个线程同时通过第一次检查后都创建实例
                    // 在锁的保护下再次确认实例是否真的没创建
                    if (instance == null) {
                        instance = new DoubleCheckLockingSingleton();
                    }
                }
            }
            return instance;
        }

        public void doSomething() {
            System.out.println("双检锁单例：执行操作");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 双检锁单例模式演示 ===\n");

        // 获取实例（第一次调用时才会创建）
        DoubleCheckLockingSingleton singleton1 = DoubleCheckLockingSingleton.getInstance();
        DoubleCheckLockingSingleton singleton2 = DoubleCheckLockingSingleton.getInstance();

        // 验证是同一个实例
        System.out.println("singleton1 == singleton2: " + (singleton1 == singleton2));
        System.out.println("singleton1.hashCode(): " + singleton1.hashCode());
        System.out.println("singleton2.hashCode(): " + singleton2.hashCode());

        singleton1.doSomething();
    }
}

