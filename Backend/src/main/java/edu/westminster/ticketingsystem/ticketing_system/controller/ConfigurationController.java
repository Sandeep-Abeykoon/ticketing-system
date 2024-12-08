package edu.westminster.ticketingsystem.ticketing_system.controller;
import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import edu.westminster.ticketingsystem.ticketing_system.service.ConfigurationService;
import edu.westminster.ticketingsystem.ticketing_system.service.SimulationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;
    private final SimulationService simulationService;

    @GetMapping
    public SystemConfiguration getConfiguration() {
        return configurationService.getConfiguration();
    }

    @PutMapping
    public ResponseEntity<?> updateSystemConfigData(@RequestBody ConfigurationData newConfigurationData) {
        if (simulationService.getSimulationStatus()) {
            // Respond with conflict if a simulation is already running
            return  ResponseEntity.status(HttpStatus.CONFLICT).
                    body("Configuration Cannot be updated while the simulation is running");
        }
        return ResponseEntity.ok(configurationService.updateSystemConfigData(newConfigurationData));
    }

    @GetMapping("/status")
    public boolean getSystemConfigStatus() {
        return configurationService.getSystemConfigStatus();
    }

}
