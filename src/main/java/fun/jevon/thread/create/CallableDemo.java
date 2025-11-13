package fun.jevon.thread.create;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableDemo {

	/**
	 * 方式三：实现 Callable 接口并重写 call 方法
	 *
	 * 特点：
	 * 1) 可以有返回值（通过 Future/FutureTask 获取）。
	 * 2) 可以抛出受检异常，在调用处通过 Future#get 捕获 ExecutionException。
	 * 3) 更适合有“计算结果”的任务。
	 */
	static class SumTask implements Callable<Integer> {
		private final int n;

		public SumTask(int n) {
			this.n = n;
		}

		@Override
		public Integer call() throws Exception {
			// 计算 1..n 的和，演示可返回结果的任务
			int sum = 0;
			for (int i = 1; i <= n; i++) {
				sum += i;
				Thread.sleep(50);
			}
			return sum;
		}
	}

	public static void main(String[] args) {
		// 使用 FutureTask 包装 Callable，并由 Thread 启动
		// FutureTask<V> 是 Java 提供的一个可执行的“可取消、可获取返回值的任务包装器”
		// FutureTask 实现了 Runnable，所以可以被线程执行
		FutureTask<Integer> futureTask = new FutureTask<>(new SumTask(10));
		Thread t = new Thread(futureTask, "T-Call-1");
		t.start();
		try {
			// get() 会阻塞直到结果就绪，可能抛出 InterruptedException/ExecutionException
			Integer result = futureTask.get();
			System.out.println(Thread.currentThread().getName() + " <- Callable结果: " + result);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			System.out.println("任务执行抛出异常: " + e.getCause());
		}
	}
}


