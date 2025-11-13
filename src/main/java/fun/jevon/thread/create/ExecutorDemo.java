package fun.jevon.thread.create;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorDemo {

	/**
	 * 方式四：使用线程池（Executor/ExecutorService）
	 *
	 * 特点：
	 * 1) 统一管理与复用线程，减少频繁创建/销毁线程的开销，提升性能。
	 * 2) 提供 execute(提交 Runnable) 与 submit(可提交 Runnable/Callable，返回 Future)。
	 * 3) 便于资源治理：支持有界队列、拒绝策略、核心/最大线程数等配置。
	 * 4) 使用完成后务必 shutdown() 以便优雅关闭线程池。
	 */
	static class PoolRunnable implements Runnable {
		private final String taskName;

		public PoolRunnable(String taskName) {
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

	static class PoolCallable implements Callable<Integer> {
		private final int n;

		public PoolCallable(int n) {
			this.n = n;
		}

		@Override
		public Integer call() throws Exception {
			// 计算 n!，演示在线程池中提交 Callable 并获取返回值
			int prod = 1;
			for (int i = 1; i <= n; i++) {
				prod *= i;
				Thread.sleep(50);
			}
			return prod;
		}
	}

	public static void main(String[] args) {
		// 演示：固定大小线程池，实际生产建议自定义 ThreadFactory/拒绝策略等
		ExecutorService pool = Executors.newFixedThreadPool(2);
		try {
			// 提交 Runnable（无返回值）
			pool.execute(new PoolRunnable("线程池Runnable-任务A"));
			pool.execute(new PoolRunnable("线程池Runnable-任务B"));
			// 提交 Callable（有返回值）
			Future<Integer> future = pool.submit(new PoolCallable(5));
			try {
				Integer result = future.get();
				System.out.println(Thread.currentThread().getName() + " <- 线程池Callable结果: " + result);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				System.out.println("线程池任务抛出异常: " + e.getCause());
			}
		} finally {
			// 及时关闭线程池，避免资源泄露
			pool.shutdown();
		}
	}
}


