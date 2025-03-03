package CreatingThreadsExamples.LambdaExpressionExample;

// Explanation:
// Uses a lambda expression to create an instance of Runnable.
// The lambda expression contains the code to be executed by the thread.
// The thread is named "Thread-5" and started with t5.start().

public class LambdaExpressionExample {
    public static void main(String[] args) {
        // Creating a thread using a lambda expression
        Thread t5 = new Thread(() -> {
            System.out.println("Running in Lambda Expression");
        }, "Thread-5");
        t5.start();
    }
}