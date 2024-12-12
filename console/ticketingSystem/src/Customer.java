/**
 * Represents a customer in the ticket management system.
 * Customers attempt to retrieve tickets from the shared TicketPool at regular intervals.
 */
public class Customer extends Person {
    private final int ticketsPerRetrieval;
    private final TicketPool ticketPool;

    /**
     * Constructs a Customer with the specified ID, interval, retrieval rate, and ticket pool.
     *
     * @param id                 Unique identifier for the customer.
     * @param interval           Interval (in milliseconds) between retrieval attempts.
     * @param ticketsPerRetrieval Number of tickets the customer tries to retrieve per attempt.
     * @param ticketPool         Shared TicketPool object for ticket retrieval.
     */
    public Customer(String id, int interval, int ticketsPerRetrieval, TicketPool ticketPool) {
        super(id, interval); // Calls the constructor of the Person superclass
        this.ticketsPerRetrieval = ticketsPerRetrieval;
        this.ticketPool = ticketPool;
    }

    /**
     * Executes the customer's ticket retrieval logic in a separate thread.
     * The customer attempts to retrieve tickets from the TicketPool at regular intervals.
     */
    @Override
    public void run() {
        while (true) {
            try {
                // Wait for the specified interval before attempting to retrieve tickets
                Thread.sleep(interval);

                boolean success = ticketPool.removeTickets(ticketsPerRetrieval);

                if (success) {
                    System.out.println("Customer " + id + " retrieved " + ticketsPerRetrieval +
                            " tickets. Remaining tickets: " + ticketPool.getTicketCount());
                } else {
                    System.out.println("Customer " + id + " could not retrieve tickets. " +
                            "Not enough tickets in the pool.");
                }
            } catch (InterruptedException e) {
                System.out.println("Customer " + id + " has stopped retrieving tickets.");
                break;
            }
        }
    }
}
