## Sleep vs. Wait

**1. Differences between `sleep()` and `wait()`:**

| Feature             | `sleep()`                                    | `wait()`                                     |
|---------------------|----------------------------------------------|----------------------------------------------|
| **Thread State**    | `TIMED_WAITING` (keeps locks)                | `WAITING` (releases locks)                   |
| **Method Location** | `Thread` class (static)                      | `Object` class (instance)                    |
| **Lock Behavior**   | Does NOT release locks                       | RELEASES locks                               |
| **Waking Up**       | Automatically wakes after specified time     | Requires explicit `notify()`/`notifyAll()`   |
| **Context Required**| Can be called anywhere                       | Must be called from a `synchronized` context |
| **Purpose**         | Pause thread execution                       | Inter-thread communication                   |
| **Interruption**    | Throws `InterruptedException` if interrupted | Throws `InterruptedException` if interrupted |

**2. Why `wait()`, `notify()`, and `notifyAll()` are in the `Object` class:**

* **Locks Are Per-Object:**
    * Every Java object can act as a monitor (lock).
    * These methods operate on the object's monitor.
* **Thread Coordination:**
    * Threads communicate by waiting/notifying on shared objects.
    * Coordination is tied to the object, not a specific thread.
* **OOP Design:**
    * Allows any object to serve as a coordination point.
    * Provides more flexibility than if the methods were in the `Thread` class.
* **Implementation Reason:**
    * The JVM implements locking at the object level.
    * Monitors are associated with objects, not threads.
* **Usage Pattern:**
    * Multiple threads often coordinate around a specific resource object.
    * Having these methods on `Object` allows the resource to act as the coordinator.
* **Language Design:**
    * Java's original concurrency design followed the monitor pattern.
    * Placing these methods in `Object` reflects this design choice.

    Race Condition
Definition
A race condition occurs when multiple threads access and manipulate shared data concurrently, and the final outcome depends on the relative timing/interleaving of their operations. It happens when the correctness of the program depends on threads executing in a specific order, but the order isn't guaranteed.

Key Points
Results from threads executing non-atomic operations on shared data
Creates unpredictable, inconsistent, and non-deterministic outcomes
One of the most common concurrency bugs
Difficult to reproduce and debug because timing varies between runs
Fixed by proper synchronization mechanisms
Simple Example
Why Race Conditions Occur
In the counter example:

Thread 1 reads counter = 5
Thread 2 reads counter = 5 (before Thread 1 writes)
Thread 1 increments and writes 6
Thread 2 increments and writes 6 (overwrites Thread 1's update)
Final value is 6 instead of 7
How to Fix Race Conditions
Synchronization: synchronized blocks/methods
Locks: ReentrantLock, etc.
Atomic variables: AtomicInteger, etc.
Thread confinement: Keep data thread-local
Immutable objects: Objects that can't be changed after creation
Real-world Analogy
Two bank tellers simultaneously processing withdrawals from the same account:

Teller A checks balance: $100
Teller B checks balance: $100
Teller A processes withdrawal of $50, records new balance $50
Teller B processes withdrawal of $80, records new balance $20
Issue: Combined withdrawal $130 from account with only $100