/**
 * Represents a vendor in the ticket management system.
 * Vendors periodically release tickets into the shared TicketPool.
 */
public class Vendor extends Person {
    private final int ticketsPerRelease;
    private final TicketPool ticketPool;

    /**
     * Constructs a Vendor with the specified ID, interval, release rate, and ticket pool.
     *
     * @param id               Unique identifier for the vendor.
     * @param interval         Interval (in milliseconds) between ticket releases.
     * @param ticketsPerRelease Number of tickets released per interval.
     * @param ticketPool       Shared TicketPool for adding tickets.
     */
    public Vendor(String id, int interval, int ticketsPerRelease, TicketPool ticketPool) {
        super(id, interval); // Initialize common properties via the Person superclass
        this.ticketsPerRelease = ticketsPerRelease;
        this.ticketPool = ticketPool;
    }

    /**
     * Executes the vendor's ticket release logic in a separate thread.
     * The vendor adds tickets to the TicketPool at regular intervals.
     */
    @Override
    public void run() {
        while (true) {
            try {
                // Wait for the specified interval before releasing tickets
                Thread.sleep(interval);

                boolean success = ticketPool.addTickets(ticketsPerRelease);

                if (success) {
                    System.out.println("Vendor " + id + " added " + ticketsPerRelease +
                            " tickets. Total tickets: " + ticketPool.getTicketCount());
                } else {
                    System.out.println("Vendor " + id + " could not add tickets. " +
                            "Not enough space in the pool.");
                }
            } catch (InterruptedException e) {
                System.out.println("Vendor " + id + " has stopped releasing tickets.");
                break; 
            }
        }
    }
}
