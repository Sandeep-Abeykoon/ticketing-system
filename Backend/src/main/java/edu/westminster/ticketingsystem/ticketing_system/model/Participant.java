package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class Participant implements Runnable {
    protected final String id;
    protected final int interval;
    protected final TicketService ticketService;
    protected final SimulationLogService logService;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                performOperation();
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logService.sendLog(getType() + " " + id + " was interrupted");
            } catch (Exception e) {
                //Todo Error Logging
            }
        }
    }

    protected abstract void performOperation();

    protected abstract String getType();
}
