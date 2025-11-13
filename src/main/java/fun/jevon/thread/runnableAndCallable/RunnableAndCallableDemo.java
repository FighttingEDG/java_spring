package fun.jevon.thread.runnableAndCallable;

import java.util.concurrent.*;

/**
 * Runnable 和 Callable 的区别演示
 *
 * 主要区别：
 * 1. 返回值：
 *    - Runnable 的 run() 方法无返回值（void）
 *    - Callable 的 call() 方法有返回值，支持泛型
 *
 * 2. 异常处理：
 *    - Runnable 的 run() 方法只能抛出运行时异常，且无法捕获处理
 *    - Callable 的 call() 方法允许抛出异常，可以获取异常信息
 */
public class RunnableAndCallableDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("=== Runnable 和 Callable 的区别演示 ===\n");

        // ========== 区别1：返回值 ==========
        System.out.println("【区别1：返回值】\n");

        // Runnable：无返回值
        System.out.println("1. Runnable - run() 方法无返回值（void）：");
        Runnable runnable = () -> {
            System.out.println("   Runnable 执行任务");
            // run() 方法返回 void，无法返回值
        };
        Thread thread = new Thread(runnable);
        thread.start();
        // 让当前线程（main 线程）等待 thread 线程执行完毕之后再继续往下走
        thread.join();

        // Callable：有返回值，支持泛型
        System.out.println("\n2. Callable - call() 方法有返回值，支持泛型：");
        Callable<String> callable = () -> {
            System.out.println("   Callable 执行任务");
            return "任务执行结果：成功"; // 可以返回任意类型（这里返回 String）
        };
        // 使用Callable接口，一般都是先创建线程池，然后submit这个callable
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(callable);
        try {
            String result = future.get(); // 获取返回值
            System.out.println("   返回值：" + result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // 不再接收新的任务，但会继续执行完已经提交的任务。
        // 等所有任务执行完毕后，线程池中的线程会被安全地销毁，释放资源。
        executor.shutdown();

        // ========== 区别2：异常处理 ==========
        System.out.println("\n" + "=".repeat(50));
        System.out.println("\n【区别2：异常处理】\n");

        // Runnable：只能抛出运行时异常，且无法捕获处理
        System.out.println("1. Runnable - run() 方法只能抛出运行时异常：");
        Runnable runnableWithException = () -> {
            System.out.println("   Runnable 抛出运行时异常");
            // 手动，主动抛出异常
            throw new RuntimeException("Runnable 中的运行时异常");
            // 注意：run() 方法不能声明抛出 checked exception（编译时异常）
            // 只能抛出运行时异常，且异常会直接传播到线程的未捕获异常处理器
        };
        Thread thread2 = new Thread(runnableWithException);
        // 处理异常
        thread2.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("   捕获到线程异常：" + e.getMessage());
        });
        thread2.start();
        thread2.join();

        // Callable：允许抛出异常，可以获取异常信息
        System.out.println("\n2. Callable - call() 方法允许抛出异常，可以获取异常信息：");
        Callable<String> callableWithException = () -> {
            System.out.println("   Callable 抛出异常");
            throw new Exception("Callable 中的异常（可以是 checked exception）");
            // call() 方法可以声明抛出 Exception，包括 checked exception
        };
        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        Future<String> future2 = executor2.submit(callableWithException);
        try {
            String result = future2.get(); // 获取返回值时，如果任务抛出异常，会在这里捕获
        } catch (ExecutionException e) {
            System.out.println("   捕获到异常：" + e.getCause().getMessage());
            System.out.println("   异常类型：" + e.getCause().getClass().getSimpleName());
            System.out.println("   ✓ 可以通过 ExecutionException.getCause() 获取原始异常");
        }
        executor2.shutdown();

        // ========== 总结 ==========
        System.out.println("\n" + "=".repeat(50));
        System.out.println("\n【总结】");
        System.out.println("Runnable：");
        System.out.println("  - run() 方法返回 void，无返回值");
        System.out.println("  - 只能抛出运行时异常，异常需要通过 UncaughtExceptionHandler 处理");
        System.out.println("  - 适用于不需要返回值的简单任务");
        System.out.println("\nCallable：");
        System.out.println("  - call() 方法有返回值，支持泛型");
        System.out.println("  - 可以抛出异常（包括 checked exception），通过 Future.get() 获取异常");
        System.out.println("  - 适用于需要返回值和异常处理的复杂任务");
    }
}

