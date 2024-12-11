package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;
import lombok.AllArgsConstructor;

import java.util.Map;

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
                logService.sendStructuredLog("THREAD_INTERRUPTED", Map.of(
                        "id", id,
                        "type", getType(),
                        "reason", "Thread interrupted"
                ));
            } catch (Exception e) {
                logService.sendStructuredLog("PARTICIPANT_ERROR", Map.of(
                        "id", id,
                        "type", getType(),
                        "errorMessage", e.getMessage()
                ));
            }
        }
    }

    protected abstract void performOperation();

    protected abstract String getType();
}
