package edu.westminster.ticketingsystem.ticketing_system.controller;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulation")
@AllArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping("/start")
    public String startSimulation(@RequestParam int numberOfVendors, @RequestParam int numberOfCustomers) {
        System.out.println("Start method called");
        try {
            simulationService.startSimulation(numberOfVendors, numberOfCustomers);
            return "Simulation started with " + numberOfVendors + " Vendors";
        } catch (Exception e) {
            return "Failed to start simulation " + e.getMessage();
        }
    }

    @PostMapping("/stop")
    public String stopSimulation() {
        simulationService.stopSimulation();
        return "Simulation stopped";
    }
}
