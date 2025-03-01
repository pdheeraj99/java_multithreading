### Q&A on Threads, Runnables, and Callables

1. **Can Threads work with both Runnables and Callables?**

    - **Runnable**:
        - Has a single method `run()` that does not return a result and cannot throw a checked exception.
        - Used directly with `Thread` objects.
      ```java
      Runnable runnableTask = () -> System.out.println("Runnable task is running");
      Thread thread = new Thread(runnableTask);
      thread.start();
      ```

    - **Callable**:
        - Has a single method `call()` that returns a result and can throw a checked exception.
        - Used with `ExecutorService` to submit tasks and get results.
      ```java
      import java.util.concurrent.*;
 
      Callable<String> callableTask = () -> "Callable task result";
      ExecutorService executorService = Executors.newSingleThreadExecutor();
      Future<String> future = executorService.submit(callableTask);
 
      try {
          String result = future.get(); // Blocks until the result is available
          System.out.println(result);
      } catch (Exception e) {
          e.printStackTrace();
      }
 
      executorService.shutdown();
      ```

2. **Are Callables only used in ExecutorService?**

    - **No**, Callables can be used in other contexts:
        - **FutureTask**: Wraps a `Callable` and can be run in a `Thread`.
      ```java
      import java.util.concurrent.*;
 
      Callable<String> callableTask = () -> "Callable task result";
      FutureTask<String> futureTask = new FutureTask<>(callableTask);
      Thread thread = new Thread(futureTask);
      thread.start();
 
      try {
          String result = futureTask.get(); // Blocks until the result is available
          System.out.println(result);
      } catch (Exception e) {
          e.printStackTrace();
      }
      ```

        - **ScheduledExecutorService**: Schedules `Callable` tasks to run after a delay or periodically.
      ```java
      import java.util.concurrent.*;
 
      ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
      Callable<String> callableTask = () -> "Callable task result";
      ScheduledFuture<String> future = scheduledExecutorService.schedule(callableTask, 5, TimeUnit.SECONDS);
 
      try {
          String result = future.get(); // Blocks until the result is available
          System.out.println(result);
      } catch (Exception e) {
          e.printStackTrace();
      }
 
      scheduledExecutorService.shutdown();
      ```

3. **Can Callables be passed to Threads like Runnables?**

    - **No**, Callables cannot be directly passed to a `Thread`. Instead, wrap a `Callable` in a `FutureTask` and pass the `FutureTask` to a `Thread`.
   ```java
   import java.util.concurrent.*;

   Callable<String> callableTask = () -> {
       Thread.sleep(1000); // Simulate computation
       return "Callable task result";
   };

   FutureTask<String> futureTask = new FutureTask<>(callableTask);
   Thread thread = new Thread(futureTask);
   thread.start();

   try {
       String result = futureTask.get(); // Blocks until the result is available
       System.out.println(result);
   } catch (Exception e) {
       e.printStackTrace();
   }