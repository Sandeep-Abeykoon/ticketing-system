package edu.westminster.ticketingsystem.ticketing_system.controller;
import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import edu.westminster.ticketingsystem.ticketing_system.service.ConfigurationService;
import edu.westminster.ticketingsystem.ticketing_system.service.SimulationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;
    private final SimulationService simulationService;

    @GetMapping
    public ResponseEntity<?> getConfiguration() {
        try {
            SystemConfiguration config = configurationService.getConfiguration();
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve configuration: " + e.getMessage());
        }
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
    public ResponseEntity<?> getSystemConfigStatus() {
        try {
            boolean status = configurationService.getSystemConfigStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve system configuration status: " + e.getMessage());
        }
    }

}
