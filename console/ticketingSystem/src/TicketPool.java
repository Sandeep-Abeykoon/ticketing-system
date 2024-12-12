import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages a thread-safe ticket pool for the ticket management system.
 * Tracks the number of tickets added, retrieved, and the current pool size.
 */
public class TicketPool {
    private final List<Integer> ticketPool;
    private final int maxCapacity;
    private int totalAdded = 0;
    private int totalRetrieved = 0;

    /**
     * Constructs a TicketPool with a specified maximum capacity.
     *
     * @param maxCapacity The maximum capacity of the ticket pool.
     */
    public TicketPool(int maxCapacity) {
        this.ticketPool = Collections.synchronizedList(new ArrayList<>());
        this.maxCapacity = maxCapacity;
    }

    /**
     * Adds a specified number of tickets to the pool.
     *
     * @param numberOfTickets Number of tickets to add.
     * @return True if tickets were successfully added, false if there is not enough space.
     */
    public synchronized boolean addTickets(int numberOfTickets) {
        if (ticketPool.size() + numberOfTickets > maxCapacity) {
            return false; // Not enough space to add tickets
        }
        for (int i = 0; i < numberOfTickets; i++) {
            ticketPool.add(1); // Add dummy tickets (represented as '1')
        }
        totalAdded += numberOfTickets; // Track the total tickets added
        return true;
    }

    /**
     * Removes a specified number of tickets from the pool.
     *
     * @param numberOfTickets Number of tickets to remove.
     * @return True if tickets were successfully removed, false if there are not enough tickets.
     */
    public synchronized boolean removeTickets(int numberOfTickets) {
        if (ticketPool.size() < numberOfTickets) {
            return false; // Not enough tickets to remove
        }
        if (numberOfTickets > 0) {
            ticketPool.subList(0, numberOfTickets).clear();
        }
        totalRetrieved += numberOfTickets; // Track the total tickets retrieved
        return true;
    }

    /**
     * @return The current number of tickets in the pool.
     */
    public synchronized int getTicketCount() {
        return ticketPool.size();
    }

    /**
     * @return The total number of tickets added to the pool.
     */
    public synchronized int getTotalAdded() {
        return totalAdded;
    }

    /**
     * @return The total number of tickets retrieved from the pool.
     */
    public synchronized int getTotalRetrieved() {
        return totalRetrieved;
    }
}
