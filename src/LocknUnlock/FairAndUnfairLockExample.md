* Explanation:
1. Shared Resource: The counter variable is the shared resource that multiple threads will try to access and modify.
2. Fair Lock: We create a ReentrantLock with the fairness parameter set to true. This ensures that threads acquire the lock in the order they requested it.
3. Unfair Lock: We create a ReentrantLock with the fairness parameter set to false. This means threads may acquire the lock out of order, potentially leading to thread starvation.
4. Increment with Fair Lock: The incrementWithFairLock method acquires the fair lock before modifying the counter and releases the lock in a finally block to ensure it is always released.
5. Increment with Unfair Lock: The incrementWithUnfairLock method acquires the unfair lock before modifying the counter and releases the lock in a finally block to ensure it is always released.
6. Threads for Fair Lock: We create multiple threads that call the incrementWithFairLock method.
7. Threads for Unfair Lock: We create multiple threads that call the incrementWithUnfairLock method.
8. Start and Join Threads: We start the threads and wait for them to finish using join.
9. Final Counter Value: We print the final value of the counter to observe the results of using fair and unfair locks.