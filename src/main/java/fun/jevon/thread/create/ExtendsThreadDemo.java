package fun.jevon.thread.create;

public class ExtendsThreadDemo {

	/**
	 * 方式一：继承 Thread 类并重写 run 方法
	 * 重写了run方法的类，类里面的线程调用start的时候，start会隐式调用重写的这个run方法
	 *
	 * 关键点是：一个thread对象只能被启动一次，所以这种继承的方式，重写的run也只能执行一次
	 * 特点：
	 * 1) 使用简单，直接调用 start() 启动线程。
	 * 2) 受限于 Java 单继承，类已经继承 Thread 后就无法再继承其他父类，灵活性较差。
	 * 3) 无法直接返回执行结果，需要自行通过共享变量或其他同步手段传递结果。
	 */
	static class MyThread extends Thread {
		private final String taskName;

		public MyThread(String taskName) {
			this.taskName = taskName;
		}

		@Override
		public void run() {
			// 模拟任务执行
			for (int i = 0; i < 3; i++) {
				System.out.println(Thread.currentThread().getName() + " -> " + taskName + " step " + i);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	public static void main(String[] args) {
		// 启动两个线程实例进行演示
		Thread t1 = new MyThread("扩展Thread方式-任务A");
		t1.setName("T-Ext-1");
		Thread t2 = new MyThread("扩展Thread方式-任务B");
		t2.setName("T-Ext-2");
		t1.start();
		t2.start();
	}
}


