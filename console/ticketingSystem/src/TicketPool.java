import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {
    private final List<Integer> ticketPool; // Thread-safe ticket pool
    private final int maxCapacity;         // Maximum capacity of the pool
    private int totalAdded = 0;            // Total tickets added
    private int totalRetrieved = 0;        // Total tickets retrieved

    public TicketPool(int maxCapacity) {
        this.ticketPool = Collections.synchronizedList(new ArrayList<>());
        this.maxCapacity = maxCapacity;
    }

    // Adds tickets to the pool
    public synchronized boolean addTickets(int numberOfTickets) {
        if (ticketPool.size() + numberOfTickets > maxCapacity) {
            return false; // Not enough space
        }
        for (int i = 0; i < numberOfTickets; i++) {
            ticketPool.add(1); // Add dummy tickets
        }
        totalAdded += numberOfTickets; // Update total added
        return true; // Successfully added
    }

    // Removes tickets from the pool
    public synchronized boolean removeTickets(int numberOfTickets) {
        if (ticketPool.size() < numberOfTickets) {
            return false; // Not enough tickets
        }
        for (int i = 0; i < numberOfTickets; i++) {
            ticketPool.remove(0);
        }
        totalRetrieved += numberOfTickets; // Update total retrieved
        return true; // Successfully removed
    }

    // Gets the current ticket count
    public synchronized int getTicketCount() {
        return ticketPool.size();
    }

    // Gets the total tickets added
    public synchronized int getTotalAdded() {
        return totalAdded;
    }

    // Gets the total tickets retrieved
    public synchronized int getTotalRetrieved() {
        return totalRetrieved;
    }
}
