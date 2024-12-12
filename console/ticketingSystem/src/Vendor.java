public class Vendor extends Person {
    private int ticketsPerRelease;
    private TicketPool ticketPool;

    public Vendor(String id, int interval, int ticketsPerRelease, TicketPool ticketPool) {
        super(id, interval);
        this.ticketsPerRelease = ticketsPerRelease;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(interval);
                boolean success = ticketPool.addTickets(ticketsPerRelease);
                if (success) {
                    System.out.println("Vendor " + id + " added " + ticketsPerRelease + " tickets. Total tickets: " + ticketPool.getTicketCount());
                } else {
                    System.out.println("Vendor " + id + " could not add tickets. Not enough space in the pool.");
                }
            } catch (InterruptedException e) {
                System.out.println("Vendor " + id + " has stopped releasing tickets.");
                break;
            }
        }
    }
}
