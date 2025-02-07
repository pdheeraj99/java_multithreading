package CreatingThreadsWithRunnableExamples.AnonymousClassExample;

// Explanation:
// Creates an instance of Runnable using an anonymous class.
// The run method contains the code to be executed by the thread.
// The thread is named "Thread-1" and started with t1.start().

public class AnonymousClassExample {
    public static void main(String[] args) {
        // Creating a thread using an anonymous class
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Running in Anonymous Class");
            }
        }, "Thread-1");
        t1.start();
    }
}