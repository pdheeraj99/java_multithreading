package ExecutorService.SingleThreadPool;

public class PolicyReminderTask implements Runnable {
    private Customer customer;

    public PolicyReminderTask(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void run() {
        sendReminder(customer);
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
}
