import java.io.Serializable;

public class Configuration implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    private int totalTickets;
    private int ticketReleaseRate;
    private int ticketReleaseInterval;
    private int customerRetrievalRate;
    private int ticketRetrievalInterval;
    private int maxTicketCapacity;

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

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getTicketReleaseInterval() {
        return ticketReleaseInterval;
    }

    public void setTicketReleaseInterval(int ticketReleaseInterval) {
        this.ticketReleaseInterval = ticketReleaseInterval;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getTicketRetrievalInterval() {
        return ticketRetrievalInterval;
    }

    public void setTicketRetrievalInterval(int ticketRetrievalInterval) {
        this.ticketRetrievalInterval = ticketRetrievalInterval;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

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
