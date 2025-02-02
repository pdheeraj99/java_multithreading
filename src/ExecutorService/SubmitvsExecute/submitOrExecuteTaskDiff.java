package ExecutorService.SubmitvsExecute;
// Executor Framework introduced in java 1.5
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class submitOrExecuteTaskDiff {
    public static void main(String[] args) {
//        This line initializes an ExecutorService with a fixed thread pool of 2 threads.
        ExecutorService executorService = Executors.newFixedThreadPool(2);

//        A Runnable task is defined which prints a message, sleeps for 1 second, and then prints another message.
        Runnable task = () -> {
            System.out.println("Task is running");
            // Simulate some work
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task completed");
        };

        // Using execute
//        The execute method is used to submit the task for execution. This method does not return any result.
        executorService.execute(task);

        // Using submit
//        The submit method is used to submit the task for execution and returns a Future object. This Future can be used to check the status of the task and retrieve the result once the task is completed.
        Future<?> future = executorService.submit(task);

        // Check if the task is done
        // The future.get() method blocks until the task is completed. After the task is done, it prints whether the task is completed using future.isDone().
        try {
            future.get(); // This will block until the task is done
            System.out.println("Task done: " + future.isDone());
        } catch (Exception e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }
}