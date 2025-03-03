Key Concepts of Daemon Threads in Java
1. Basics & Definition
Daemon threads are background threads that don't prevent JVM from exiting
JVM exits when only daemon threads remain
Main thread is non-daemon by default
Use setDaemon(true) before starting a thread to make it a daemon
2. Behavior & Characteristics
Daemon threads are abruptly terminated when JVM exits
Cannot change daemon status after thread has started
Child threads inherit daemon status from parent thread
Daemon threads may not execute finally blocks if JVM exits
Thread pools can be configured with daemon threads using custom ThreadFactory
3. Use Cases
Background services (auto-save, monitoring)
Periodic maintenance tasks
Cleanup operations
Garbage collection
Timer services
Watchdog threads
Cache maintenance
4. System Daemon Threads
Finalizer (runs object finalize() methods)
Reference Handler (processes reference objects)
Signal Dispatcher (handles JVM signals)
Garbage Collector threads
Common-Cleaner (cleans direct ByteBuffers)
Attach Listener (for profilers and debugging tools)
5. Best Practices
DO provide a graceful shutdown mechanism for daemon threads
DO use daemon threads for non-critical background services
DO check thread interruption signals in daemon threads
DON'T use daemon threads for tasks requiring guaranteed completion
DON'T use daemon threads for critical resource operations
DON'T rely on finally blocks executing in daemon threads
6. Thread Status Checking
Use thread.isDaemon() to check if a thread is a daemon
All threads display daemon status in thread dumps
Can only set daemon status before the thread starts
7. Lifecycle Management
No special method to stop daemon threads (use standard interruption)
Daemon status only affects behavior when JVM is shutting down
During normal execution, daemon threads behave like regular threads
Consider using shutdown flags for clean daemon thread termination
8. Practical Considerations
Daemon threads can improve application exit behavior by not blocking shutdown
Services like timers can be created as daemons to avoid memory leaks
Thread pools with daemon threads don't block application exit
Frameworks often use daemon threads for background tasks
9. Common Patterns with Daemon Threads
Worker-listener pattern (background processing)
Watchdog pattern (monitoring other threads)
Cleanup pattern (periodic resource management)
Event dispatching (background event processing)
Cache eviction (background memory management)
This concise summary covers all essential aspects of daemon threads in Java from basic properties to advanced patterns and best practices.