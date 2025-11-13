package fun.jevon.thread.create;

public class RunnableDemo {

	/**
	 * 方式二：实现 Runnable 接口并重写 run 方法
	 *
	 * public void run() {
	 *     if (target != null) {   // target 是构造 Thread 时传入的 Runnable
	 *         target.run();       // 调用 Runnable 的 run 方法
	 *     }
	 * }
	 * 关键点：这里是每个线程都调用run方法，但是因为这边的类实现了Runnable，这里的Runnable判断不再为null
	 * 所以不是使用默认的run，而是runnable的run
	 *
	 * 特点：
	 * 1) 避免单继承局限性：业务类可以实现 Runnable 的同时继承其他父类，灵活性更好。
	 * 2) 线程与任务解耦：任务只负责逻辑，线程由 Thread 承载，便于复用与管理。
	 * 3) 同样不直接返回结果，如需结果可配合共享对象或使用 Callable/Future。
	 */
	static class MyRunnable implements Runnable {
		private final String taskName;

		public MyRunnable(String taskName) {
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
		// 使用 Thread 包裹 Runnable 任务
		Thread t1 = new Thread(new MyRunnable("实现Runnable方式-任务A"), "T-Run-1");
		Thread t2 = new Thread(new MyRunnable("实现Runnable方式-任务B"), "T-Run-2");
		t1.start();
		t2.start();
	}
}


