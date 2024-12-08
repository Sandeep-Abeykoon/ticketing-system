package edu.westminster.ticketingsystem.ticketing_system.controller;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationService;
import edu.westminster.ticketingsystem.ticketing_system.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/simulation")
public class SimulationController {

    private final SimulationService simulationService;
    private  final TicketService ticketService;

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
    public ResponseEntity<Map<String, Object>> getSimulationStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("isRunning", simulationService.getSimulationStatus());
        response.put("ticketCount", ticketService.geTicketCount());
        response.put("numberOfVendors", simulationService.getNumberOfVendors());
        response.put("numberOfCustomers", simulationService.getNumberOfCustomers());
        return ResponseEntity.ok(response);
    }
}
