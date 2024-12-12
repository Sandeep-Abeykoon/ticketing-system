package edu.westminster.ticketingsystem.ticketing_system.controller;

import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import edu.westminster.ticketingsystem.ticketing_system.service.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ConfigurationController provides REST endpoints for managing the system's configuration.
 * This controller allows clients to retrieve the current configuration, update the configuration,
 * and check the configuration status of the system.
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    /**
     * Retrieves the current system configuration.
     *
     * @return ResponseEntity containing the system configuration or an error message.
     */
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

    /**
     * Updates the system configuration with new data.
     *
     * @param newConfigurationData The new configuration data to be updated.
     * @return ResponseEntity containing the updated configuration or an error message.
     */
    @PutMapping
    public ResponseEntity<?> updateSystemConfigData(@RequestBody ConfigurationData newConfigurationData) {
        try {
            return ResponseEntity.ok(configurationService.updateSystemConfigData(newConfigurationData));
        } catch (IllegalStateException e) {
            // Handles state-related errors
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Handles input validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input Error: " + e.getMessage());
        } catch (Exception e) {
            // Handles unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update configuration: " + e.getMessage());
        }
    }

    /**
     * Retrieves the status of the system configuration.
     *
     * @return ResponseEntity containing a boolean indicating whether the system is configured or an error message.
     */
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
