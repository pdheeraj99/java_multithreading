package CreatingThreadsWithRunnableExamples.MethodReferenceExample;

// Explanation:
// Uses a method reference example::runMethod to refer to the runMethod of the example object.
// The runMethod contains the code to be executed by the thread.
// The thread is named "Thread-4" and started with t4.start().

public class MethodReferenceExample {
    public static void main(String[] args) {
        MethodReferenceExample example = new MethodReferenceExample();
        Thread t4 = new Thread(example::runMethod, "Thread-4");
        t4.start();
    }

    // Method to be referenced
    public void runMethod() {
        System.out.println("Running in Method Reference");
    }
}