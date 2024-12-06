package edu.westminster.ticketingsystem.ticketing_system.component;

import edu.westminster.ticketingsystem.ticketing_system.model.Vendor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Simulation implements CommandLineRunner {
    private final TicketPool ticketPool;

    public Simulation(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @Override
    public void run(String... args) {
        // Create vendor threads
        Thread vendor1 = new Thread(new Vendor("Vendor1", 5, 2000, ticketPool));
        Thread vendor2 = new Thread(new Vendor("Vendor2", 3, 3000, ticketPool));

        // Start vendor threads
        vendor1.start();
        vendor2.start();

        // Let the simulation run for 15 seconds
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Interrupt vendor threads
        vendor1.interrupt();
        vendor2.interrupt();
    }
}
