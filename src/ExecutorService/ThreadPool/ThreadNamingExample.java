package ExecutorService.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadNamingExample {
    public static void main(String[] args) {
        CustomThreadFactory threadFactory = new CustomThreadFactory("CustomPool");
        ExecutorService executorService = Executors.newFixedThreadPool(3, threadFactory);

        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Running task in " + threadName);
            try {
                Thread.sleep(1000); // Simulate some work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task completed in " + threadName);
        };

        for (int i = 0; i < 5; i++) {
            executorService.submit(task);
        }

        // By manually doing shutdown it will terminate all the threads in the threadPool. otherwise the threads keep on running
        // If you didnt keep below line the threads keep on running
        executorService.shutdown();
    }
}
