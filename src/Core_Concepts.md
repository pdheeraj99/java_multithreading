# Java Multithreading

## Introduction

Multithreading in Java is a feature that allows concurrent execution of two or more parts of a program for maximum utilization of CPU. Each part of such program is called a thread, and each thread defines a separate path of execution.

## Core Concepts

### Thread Creation

In Java, threads can be created in two ways:

* Extending the `Thread` class
* Implementing the `Runnable` interface

### Thread States

* **New:** Thread is created but not started
* **Runnable:** Thread is ready to run
* **Blocked:** Thread is waiting for a monitor lock
* **Waiting:** Thread is waiting indefinitely for another thread
* **Timed Waiting:** Thread is waiting for a specified time
* **Terminated:** Thread has completed execution

### Thread Methods

* `start()`: Begins execution of the thread
* `run()`: Contains the code to be executed
* `sleep()`: Pauses thread execution for specified time
* `join()`: Waits for thread to die
* `yield()`: Temporarily pauses current thread to allow other threads to execute

### Synchronization

* **Critical Section:** A code segment that accesses shared resources
* **Synchronized Methods:** Use `synchronized` keyword
* **Synchronized Blocks:** Synchronize specific blocks of code
* **Locks:** More flexible control with `Lock` interface
* **Atomic Operations:** Thread-safe operations from `java.util.concurrent.atomic`

### Thread Communication

* `wait()`: Makes thread wait until `notify`/`notifyAll` is called
* `notify()`: Wakes up a single waiting thread
* `notifyAll()`: Wakes up all waiting threads

### Thread Pools

The `java.util.concurrent` package provides thread pool implementations:

* **Fixed Thread Pool:** Fixed number of threads
* **Cached Thread Pool:** Creates threads as needed
* **Scheduled Thread Pool:** For delayed or periodic tasks

### Concurrent Collections

* `ConcurrentHashMap`
* `CopyOnWriteArrayList`
* `BlockingQueue` implementations

### Common Issues

* **Race Conditions:** Multiple threads accessing shared data
* **Deadlocks:** Two or more threads blocked forever
* **Livelocks:** Threads actively trying to resolve a conflict
* **Starvation:** Thread unable to gain regular access to shared resources