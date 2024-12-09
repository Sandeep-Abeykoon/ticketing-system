package edu.westminster.ticketingsystem.ticketing_system.controller;
import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import edu.westminster.ticketingsystem.ticketing_system.service.ConfigurationService;
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
        try {
            return ResponseEntity.ok(configurationService.updateSystemConfigData(newConfigurationData));
        } catch (IllegalStateException e) {
            // state-related errors
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Input validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input Error: " + e.getMessage());
        } catch (Exception e) {
            // Unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update configuration: " + e.getMessage());
        }
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

