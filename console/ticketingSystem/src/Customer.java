public class Customer extends Person {
    private int ticketsPerRetrieval;
    private TicketPool ticketPool;

    public Customer(String id, int interval, int ticketsPerRetrieval, TicketPool ticketPool) {
        super(id, interval);
        this.ticketsPerRetrieval = ticketsPerRetrieval;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(interval);
                boolean success = ticketPool.removeTickets(ticketsPerRetrieval);
                if (success) {
                    System.out.println("Customer " + id + " retrieved " + ticketsPerRetrieval + " tickets. Remaining tickets: " + ticketPool.getTicketCount());
                } else {
                    System.out.println("Customer " + id + " could not retrieve tickets. Not enough tickets in the pool.");
                }
            } catch (InterruptedException e) {
                System.out.println("Customer " + id + " has stopped retrieving tickets.");
                break;
            }
        }
    }
}
