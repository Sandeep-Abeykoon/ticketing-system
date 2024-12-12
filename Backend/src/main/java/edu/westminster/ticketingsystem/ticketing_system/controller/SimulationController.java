package edu.westminster.ticketingsystem.ticketing_system.controller;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SimulationController provides REST endpoints to control and monitor the simulation.
 * This controller handles operations such as starting, stopping, resetting the simulation,
 * and fetching the simulation's status.
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    /**
     * Starts the simulation with the specified number of vendors, customers, and VIP customers.
     *
     * @param numberOfVendors Number of vendors to include in the simulation.
     * @param numberOfCustomers Number of normal customers to include in the simulation.
     * @param numberOfVIPCustomers Number of VIP customers to include in the simulation.
     * @return ResponseEntity with a success message or error message if the operation fails.
     */
    @PostMapping("/start")
    public ResponseEntity<?> startSimulation(@RequestParam int numberOfVendors,
                                             @RequestParam int numberOfCustomers,
                                             @RequestParam int numberOfVIPCustomers) {
        try {
            simulationService.startSimulation(numberOfVendors, numberOfCustomers, numberOfVIPCustomers);
            return ResponseEntity.ok("Simulation started successfully.");
        } catch (IllegalStateException e) {
            // Handles state-related errors
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Handles input validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input Error: " + e.getMessage());
        } catch (Exception e) {
            // Handles unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start simulation: " + e.getMessage());
        }
    }

    /**
     * Stops the running simulation.
     *
     * @return ResponseEntity with a success message or error message if the operation fails.
     */
    @PostMapping("/stop")
    public ResponseEntity<?> stopSimulation() {
        try {
            simulationService.stopSimulation();
            return ResponseEntity.ok("Simulation stopped successfully.");
        } catch (IllegalStateException e) {
            // Handles state-related errors
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        } catch (Exception e) {
            // Handles unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to stop simulation: " + e.getMessage());
        }
    }

    /**
     * Fetches the current status of the simulation.
     *
     * @return ResponseEntity with the simulation status details or an error message if the operation fails.
     */
    @GetMapping("/status")
    public ResponseEntity<?> getSimulationStatus() {
        try {
            Map<String, Object> response = simulationService.getSimulationStatusDetails();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handles unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch simulation status: " + e.getMessage()));
        }
    }

    /**
     * Resets the ticket pool and clears all simulation data.
     *
     * @return ResponseEntity with a success message or error message if the operation fails.
     */
    @PostMapping("/reset")
    public ResponseEntity<?> resetSimulation() {
        try {
            Map<String, Object> response = simulationService.resetTicketPoolData();
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            // Handles state-related errors
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        } catch (Exception e) {
            // Handles unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to reset simulation: " + e.getMessage());
        }
    }
}
