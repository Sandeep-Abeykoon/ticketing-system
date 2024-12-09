package edu.westminster.ticketingsystem.ticketing_system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfigurationService {

    private final FileService fileService;
    private final SystemConfiguration systemConfiguration;
    private final ObjectMapper objectMapper;
    private final ConfigurationValidationService validationService;

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

    public void saveConfiguration() {
        boolean saved = fileService.writeConfiguration(systemConfiguration.getConfigurationData());
        if (saved) {
            System.out.println("Configuration saved successfully.");
        } else {
            System.out.println("Failed to save configuration.");
        }
    }

    public SystemConfiguration getConfiguration() {
        return this.systemConfiguration;
    }

    public boolean getSystemConfigStatus() {
        return this.systemConfiguration.isSystemConfigured();
    }

    public SystemConfiguration updateSystemConfigData(ConfigurationData newConfigurationData) {
        // Validate the ConfigurationData before updating
        validationService.validateConfigurationData(newConfigurationData);

        try {
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
