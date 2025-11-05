package fun.jevon.singleton;

/**
 * 普通多例类（Multi-Instance）最简单演示
 * 
 * 关键说明：
 * 1. 普通多例类不使用 getInstance() 方法
 *    - 设计模式上规定的，不是java语法规定，因为实际上getInstance都是手写的方法
 *    - 因此不需要 getInstance() 方法
 * 
 * 2. 普通多例类的特点：
 *    - 使用 new 关键字创建实例
 *    - 每次 new 都会创建新的对象
 *    - 可以创建任意多个不同的实例
 *    - 构造函数是公有的，可以任意调用
 * 
 * 3. 判断是否是同一个对象：
 *    - 使用 == 比较引用地址
 *    - == 返回 true：引用地址相同，是同一个对象
 *    - == 返回 false：引用地址不同，是不同的对象
 */
public class MultiInstanceDemo {

    // 普通多例类
    static class Person {
        // 加上这个静态方法，才能用 Person.getInstance(...) 调用
        public static Person getInstance() {
            // 每次new新实例，才是多例
            return new Person(); // 每次返回新实例（多例）
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 多例 getInstance 演示 ===\n");

        // 使用 getInstance 创建多个实例
        Person p1 = Person.getInstance();
        Person p2 = Person.getInstance();

        // 验证是否是不同对象
        System.out.println("p1 == p2: " + (p1 == p2)); // false
        System.out.println("说明p1和p2不是同一个实例");

        // 设计模式上规定的，不是java语法规定，因为实际上getInstance都是手写的方法
        System.out.println("- getInstance() 是单例模式特有的方法");


    }
}

