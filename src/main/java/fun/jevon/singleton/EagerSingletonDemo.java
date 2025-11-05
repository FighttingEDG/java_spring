package fun.jevon.singleton;

/**
 * （1）饿汉式单例模式（Eager Singleton）最简单演示
 *
 * 特点：
 * - 类加载时就创建实例，无论是否使用
 * - 使用 static final 变量，天然线程安全
 * - 无锁、无同步开销，性能最高
 */
public class EagerSingletonDemo {

    /**
     * 饿汉式单例：类加载时立即创建实例
     * 所以饿汉模式实质上是利用了static在类初始化变量
     * final是锦上添花的
     */
    static class EagerSingleton {
        // 私有静态 final 变量
        // static功能：类加载时立即初始化
        // 饿汉是直接new自己类
        // 懒汉是调用的时候才会new
        private static final EagerSingleton instance = new EagerSingleton();

        // 私有构造函数，防止外部创建实例
        private EagerSingleton() {
            System.out.println("饿汉式单例：实例已创建（类加载时）");
        }

        // 公共静态方法，返回唯一实例
        // 不是第一次调用getInstance时候才new，而是第一次在加载类初始化了静态变量
        public static EagerSingleton getInstance() {
            return instance;
        }

        public void doSomething() {
            System.out.println("饿汉式单例：执行操作");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 饿汉式单例模式演示 ===\n");

        // 获取实例（实际上在类加载时已经创建好了）
        EagerSingleton singleton1 = EagerSingleton.getInstance();
        EagerSingleton singleton2 = EagerSingleton.getInstance();

        // 验证是同一个实例
        System.out.println("singleton1 == singleton2: " + (singleton1 == singleton2));
        System.out.println("singleton1.hashCode(): " + singleton1.hashCode());
        System.out.println("singleton2.hashCode(): " + singleton2.hashCode());
        System.out.println("hashCode一致，说明饿汉式单例的全局唯一性是成立的");

        singleton1.doSomething();
    }
}

