# Semaphores in Java

## Definition
1. A semaphore is a synchronization mechanism that controls access to resources by maintaining a set of permits.

## Types of Semaphores
1. Counting Semaphore: Maintains multiple permits
2. Binary Semaphore: Has only 1 permit (acts like a mutex/lock)

## Core Operations
1. acquire(): Gets a permit (blocks if none available)
2. release(): Returns a permit to the semaphore
3. availablePermits(): Checks how many permits are available

## Additional Methods
1. tryAcquire(): Non-blocking attempt to get a permit
2. acquireUninterruptibly(): Cannot be interrupted while waiting

## Creating a Semaphore
1. Basic semaphore: 
```java
Semaphore semaphore = new Semaphore(3);
```
2. Fair semaphore:
```java
Semaphore fairSemaphore = new Semaphore(3, true);
```

## Common Use Cases
1. Resource pools (connection pools, thread pools)
2. Rate limiting (control number of concurrent operations)
3. Implementing producer-consumer patterns
4. Controlling access to limited resources

## Simple Mental Model
1. Thread takes a token when it needs a resource (acquire())
2. If box is empty, thread waits until a token is returned
3. Thread returns the token when done (release())

## Benefits
1. Controls access to limited resources
2. Prevents resource exhaustion
3. Manages thread waiting automatically
4. Makes concurrent code more predictable