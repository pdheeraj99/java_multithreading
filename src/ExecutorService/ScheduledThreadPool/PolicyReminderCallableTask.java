package ExecutorService.ScheduledThreadPool;

import java.util.concurrent.Callable;

public class PolicyReminderCallableTask implements Callable<String> {
    private Customer customer;

    public PolicyReminderCallableTask(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String call() {
        return sendReminder(customer);
    }

    private String sendReminder(Customer customer) {
        System.out.println("Sending policy reminder to " + customer.getName() + " at " + customer.getEmail());
        // Simulate sending email
        try {
            Thread.sleep(1000); // Simulate time taken to send email
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Reminder sent to " + customer.getName();
    }
}