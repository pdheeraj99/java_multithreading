package SemaphoreExamples;

import java.util.concurrent.Semaphore;

/**
 * This example demonstrates a semaphore controlling access to a limited number
 * of copies of a popular book in a library.
 */
public class LibraryBooks {

    public static void main(String[] args) {
        // Create a library with only 2 copies of "Java Concurrency in Practice"
        Library library = new Library(2);
        
        // Create 5 students who all want to borrow the same book
        for (int i = 1; i <= 5; i++) {
            Thread studentThread = new Thread(new Student(i, library));
            studentThread.start();
        }
    }
    
    /**
     * Represents a library with limited copies of a popular book.
     */
    static class Library {
        // The semaphore tracks the available book copies
        private final Semaphore availableBooks;
        private final int totalCopies;
        private final String bookTitle = "Java Concurrency in Practice";
        
        public Library(int copies) {
            // Initialize the semaphore with the number of book copies
            availableBooks = new Semaphore(copies, true);
            totalCopies = copies;
        }
        
        /**
         * Student attempts to borrow the book.
         * If no copies are available, the student waits.
         */
        public void borrowBook(int studentId) {
            try {
                System.out.printf("Student #%d is looking for '%s'. Available copies: %d/%d\n", 
                        studentId, bookTitle, availableBooks.availablePermits(), totalCopies);
                
                // Try to acquire a book copy (will wait if none are available)
                availableBooks.acquire();
                
                // Book successfully borrowed
                System.out.printf("Student #%d BORROWED '%s'. Available copies: %d/%d\n", 
                        studentId, bookTitle, availableBooks.availablePermits(), totalCopies);
                
                // Student reads the book for some time
                System.out.printf("Student #%d is reading the book...\n", studentId);
                Thread.sleep((long) (Math.random() * 3000) + 2000);
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        /**
         * Student returns the book to the library
         */
        public void returnBook(int studentId) {
            // Return the book (release a permit)
            availableBooks.release();
            
            System.out.printf("Student #%d RETURNED '%s'. Available copies: %d/%d\n", 
                    studentId, bookTitle, availableBooks.availablePermits(), totalCopies);
        }
    }
    
    /**
     * Represents a student who wants to borrow and read the book.
     */
    static class Student implements Runnable {
        private final int studentId;
        private final Library library;
        
        public Student(int studentId, Library library) {
            this.studentId = studentId;
            this.library = library;
        }
        
        @Override
        public void run() {
            // Wait a bit before going to the library (students arrive at different times)
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // Borrow the book
            library.borrowBook(studentId);
            
            // Return the book after reading
            library.returnBook(studentId);
            
            // Student goes home and studies notes
            System.out.printf("Student #%d is now studying their notes at home.\n", studentId);
        }
    }
}
