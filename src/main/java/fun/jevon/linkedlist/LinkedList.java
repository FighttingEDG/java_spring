package fun.jevon.linkedlist;

// 链表demo，手写实现的链表，在java原生中是没有链表的
// java原生有的：数组（Array）、ArrayList、LinkedList、HashMap、HashSet、TreeMap、TreeSet、PriorityQueue、Deque
public class LinkedList {
    // 不具体指定类型
    static class Node<E> {
        // 声明变量，此时值为null，此时是引用槽位分配，但对象未创建
        E data;
        Node<E> next;

        // 构造器，初始化对象状态，没有任何字段修饰符，但是有访问修饰符public/private/protected
        // 必须是类名，没返回值，不写void
        // 写了void就是普通方法不再是构造器了
        Node(E data) {
            this.data = data;
        }
    }

    public static void main(String[] args) {
        // 创建节点
        Node<String> node1 = new Node<>("A");
        Node<String> node2 = new Node<>("C");
        Node<String> node3 = new Node<>("B");
        // 修改 next 指针，形成链表：A -> B -> C
        node1.next = node2;
        node2.next = node3;
        // 遍历链表并获取数据
        Node<String> current = node1;
        while (current != null) {
            System.out.println(current.data); // 输出节点数据
            current = current.next;           // 移动到下一个节点
        }

        // 修改链表结构：让 A -> C
        node1.next = node3;
        System.out.println("修改后链表：");
        current = node1;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
}
