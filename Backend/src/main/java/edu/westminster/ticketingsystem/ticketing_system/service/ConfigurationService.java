package edu.westminster.ticketingsystem.ticketing_system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ConfigurationService handles operations related to loading, saving, retrieving,
 * and updating the system configuration.
 * This service ensures that the configuration is correctly validated, persisted,
 * and accessed throughout the application lifecycle.
 */
@Service
@AllArgsConstructor
public class ConfigurationService {

    private final FileService fileService;
    private final SystemConfiguration systemConfiguration;
    private final ObjectMapper objectMapper;
    private final ConfigurationValidationService validationService;
    private final SimulationService simulationService;

    /**
     * Loads the system configuration from a file during application initialization.
     * If the configuration is successfully loaded, the system is marked as configured.
     * Otherwise, default configuration values are used.
     */
    @PostConstruct
    public void loadConfiguration() {
        boolean loaded = fileService.readConfiguration(systemConfiguration.getConfigurationData());
        if (loaded) {
            this.systemConfiguration.setSystemConfigured(true);
            System.out.println("Configuration loaded successfully.");
        } else {
            System.out.println("Failed to load configuration. Using default values.");
        }
    }

    /**
     * Saves the current system configuration to a file.
     * Prints a message indicating whether the operation was successful or failed.
     */
    public void saveConfiguration() {
        boolean saved = fileService.writeConfiguration(systemConfiguration.getConfigurationData());
        if (saved) {
            System.out.println("Configuration saved successfully.");
        } else {
            System.out.println("Failed to save configuration.");
        }
    }

    /**
     * Retrieves the current system configuration.
     *
     * @return The current SystemConfiguration instance.
     */
    public SystemConfiguration getConfiguration() {
        return this.systemConfiguration;
    }

    /**
     * Checks if the system is fully configured.
     *
     * @return true if the system is configured, false otherwise.
     */
    public boolean getSystemConfigStatus() {
        return this.systemConfiguration.isSystemConfigured();
    }

    /**
     * Updates the system configuration with new data.
     * Ensures that the configuration is validated and cannot be updated while a simulation
     * is running. The updated configuration is persisted to a file.
     *
     * @param newConfigurationData The new configuration data to update.
     * @return The updated SystemConfiguration instance.
     * @throws IllegalStateException if the simulation is running.
     * @throws RuntimeException if the update operation fails.
     */
    public SystemConfiguration updateSystemConfigData(ConfigurationData newConfigurationData) {
        if (simulationService.getSimulationStatus()) {
            throw new IllegalStateException("Configuration cannot be updated while a simulation is already running");
        }

        // Validate the new configuration data
        validationService.validateConfigurationData(newConfigurationData);

        try {
            // Update the existing configuration data with the new values
            objectMapper.readerForUpdating(systemConfiguration.getConfigurationData())
                    .readValue(objectMapper.writeValueAsString(newConfigurationData));
            saveConfiguration();
            this.systemConfiguration.setSystemConfigured(true);
        } catch (Exception e) {
            System.out.println("Failed to update data: " + e.getMessage());
            throw new RuntimeException("Failed to update configuration data", e);
        }

        return this.systemConfiguration;
    }
}
