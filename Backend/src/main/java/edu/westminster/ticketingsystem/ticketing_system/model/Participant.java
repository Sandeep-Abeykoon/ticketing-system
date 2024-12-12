package edu.westminster.ticketingsystem.ticketing_system.model;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationLogService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * The Participant class represents a generic participant in the ticketing system simulation.
 * Participants are modeled as threads that perform specific operations at regular intervals.
 * This abstract class provides a common structure for all types of participants, such as customers or vendors.
 */
@AllArgsConstructor
public abstract class Participant implements Runnable {

    /**
     * Unique identifier for the participant.
     */
    protected final String id;

    /**
     * The interval (in milliseconds) at which the participant performs operations.
     */
    protected final int interval;

    /**
     * The ticket service used for performing ticket-related operations.
     */
    protected final TicketService ticketService;

    /**
     * The simulation log service used for logging participant activities.
     */
    protected final SimulationLogService logService;

    /**
     * The main run method that continuously performs the participant's operations
     * until the thread is interrupted.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                performOperation();
                Thread.sleep(interval); // Wait for the next operation cycle
            } catch (InterruptedException e) {
                // Handle thread interruption
                Thread.currentThread().interrupt();
                logService.sendStructuredLog("THREAD_INTERRUPTED", Map.of(
                        "id", id,
                        "type", getType(),
                        "reason", "Thread interrupted"
                ));
            } catch (Exception e) {
                // Log any other unexpected errors
                logService.sendStructuredLog("PARTICIPANT_ERROR", Map.of(
                        "id", id,
                        "type", getType(),
                        "errorMessage", e.getMessage()
                ));
            }
        }
    }

    /**
     * Abstract method to define the specific operation performed by the participant.
     */
    protected abstract void performOperation();

    /**
     * Abstract method to define the type of the participant (e.g., Customer, Vendor).
     *
     * @return The type of the participant as a string.
     */
    protected abstract String getType();

}
