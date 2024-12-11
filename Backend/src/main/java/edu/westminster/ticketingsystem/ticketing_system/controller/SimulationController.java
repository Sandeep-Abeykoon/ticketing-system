package edu.westminster.ticketingsystem.ticketing_system.controller;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping("/start")
    public ResponseEntity<?> startSimulation(@RequestParam int numberOfVendors,
                                             @RequestParam int numberOfCustomers,
                                             @RequestParam int numberOfVIPCustomers) {
        try {
            simulationService.startSimulation(numberOfVendors, numberOfCustomers, numberOfVIPCustomers);
            return ResponseEntity.ok("Simulation started successfully.");
        } catch (IllegalStateException e) {
            // State-related errors
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Input validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input Error: " + e.getMessage());
        } catch (Exception e) {
            // Unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start simulation: " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<?> stopSimulation() {
        try {
            simulationService.stopSimulation();
            return ResponseEntity.ok("Simulation stopped successfully.");
        } catch (IllegalStateException e) {
            // State-related errors
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        } catch (Exception e) {
            // Unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to stop simulation: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getSimulationStatus() {
        try {
            Map<String, Object> response = simulationService.getSimulationStatusDetails();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch simulation status: " + e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetSimulation() {
        try {
            Map<String, Object> response = simulationService.resetTicketPoolData();
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to reset simulation: " + e.getMessage());
        }
    }
}
