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

    // Automatically load configuration after Spring context initialization
    @PostConstruct
    public void loadConfiguration() {
        boolean loaded = fileService.readConfiguration(systemConfiguration.getConfigurationData());
        if (loaded) {
            systemConfiguration.setSystemConfigured(true);
            System.out.println("Configuration loaded successfully.");
        } else {
            systemConfiguration.setSystemConfigured(false);
            System.out.println("Failed to load configuration. Using default values.");
        }
    }


    // Save configuration to JSON file
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

    public boolean getSystemStatus() {
        return this.systemConfiguration.isSystemConfigured();
    }

    public void updateSystemStatus(boolean systemConfigured) {
        systemConfiguration.setSystemConfigured(systemConfigured);
    }

    public ConfigurationData updateSystemConfigData(ConfigurationData newConfigurationData) {
        try {
            objectMapper.readerForUpdating(systemConfiguration.getConfigurationData()).readValue(objectMapper.writeValueAsString(newConfigurationData));
            saveConfiguration();
        } catch (Exception e) {
            System.out.println("Failed to update data");
        }
        return null;
    }
}
