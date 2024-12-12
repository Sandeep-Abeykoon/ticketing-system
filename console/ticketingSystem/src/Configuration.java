import java.io.Serial;
import java.io.Serializable;

/**
 * Represents the configuration settings for the ticket management system.
 * Implements Serializable to allow saving and loading the configuration to/from a file.
 */
public class Configuration implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int totalTickets;
    private final int ticketReleaseRate;
    private final int ticketReleaseInterval;
    private final int customerRetrievalRate;
    private final int ticketRetrievalInterval;
    private final int maxTicketCapacity;

    /**
     * Constructor to initialize the configuration settings.
     *
     * @param totalTickets          Initial total number of tickets
     * @param ticketReleaseRate     Number of tickets released per interval
     * @param ticketReleaseInterval Interval (in ms) for releasing tickets
     * @param customerRetrievalRate Number of tickets customers can retrieve per interval
     * @param ticketRetrievalInterval Interval (in ms) for ticket retrieval by customers
     * @param maxTicketCapacity     Maximum capacity of the ticket pool
     */
    public Configuration(int totalTickets, int ticketReleaseRate,
                         int ticketReleaseInterval, int customerRetrievalRate,
                         int ticketRetrievalInterval, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketReleaseInterval = ticketReleaseInterval;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketRetrievalInterval = ticketRetrievalInterval;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * @return Number of tickets released per interval.
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * @return Interval (in ms) for releasing tickets.
     */
    public int getTicketReleaseInterval() {
        return ticketReleaseInterval;
    }

    /**
     * @return Number of tickets customers can retrieve per interval.
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * @return Interval (in ms) for ticket retrieval by customers.
     */
    public int getTicketRetrievalInterval() {
        return ticketRetrievalInterval;
    }

    /**
     * @return Maximum capacity of the ticket pool.
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    /**
     * Provides a string representation of the configuration for display purposes.
     *
     * @return String representation of the configuration settings.
     */
    @Override
    public String toString() {
        return "Configuration {" +
                "totalTickets=" + totalTickets +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", ticketReleaseInterval=" + ticketReleaseInterval +
                ", customerRetrievalRate=" + customerRetrievalRate +
                ", ticketRetrievalInterval=" + ticketRetrievalInterval +
                ", maxTicketCapacity=" + maxTicketCapacity +
                '}';
    }
}
