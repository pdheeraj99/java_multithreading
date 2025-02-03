package ExecutorService.SubmitvsExecute.Submit.Callable;

import java.util.concurrent.Callable;

public class PolicyReminderTask implements Callable<Object> {
    private Customer customer;

    public PolicyReminderTask(Customer customer) {
        this.customer = customer;
    }

    private void sendReminder(Customer customer) {
        System.out.println("Sending policy reminder to " + customer.getName() + " at " + customer.getEmail());
        // Simulate sending email
        try {
            Thread.sleep(1000); // Simulate time taken to send email
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Reminder sent to " + customer.getName());
    }

    @Override
    public Object call() throws Exception {
        sendReminder(customer);
        return null;
    }
}
