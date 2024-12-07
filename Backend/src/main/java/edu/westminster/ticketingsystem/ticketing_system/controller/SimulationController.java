package edu.westminster.ticketingsystem.ticketing_system.controller;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping("/start")
    public ResponseEntity<String> startSimulation(@RequestParam int numberOfVendors, @RequestParam int numberOfCustomers) {
        try {
            simulationService.startSimulation(numberOfVendors, numberOfCustomers);
            return ResponseEntity.ok("Simulation started");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start simulation " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSimulation() {
        try {
            simulationService.stopSimulation();
            return ResponseEntity.ok("Simulation stopped");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to stop simulation: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> getSimulationStatus() {
        return ResponseEntity.ok(simulationService.getSimulationStatus());
    }
}
