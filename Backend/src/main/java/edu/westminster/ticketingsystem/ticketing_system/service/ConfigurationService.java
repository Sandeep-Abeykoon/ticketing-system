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
            this.systemConfiguration.setSystemConfigured(true);
            System.out.println("Configuration loaded successfully.");
        } else {
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

    public boolean getSystemConfigStatus() {
        return this.systemConfiguration.isSystemConfigured();
    }

    public SystemConfiguration updateSystemConfigData(ConfigurationData newConfigurationData) {
        //Todo Validate the ConfigurationData before setting
        this.systemConfiguration.setSystemConfigured(true);
        try {
            objectMapper.readerForUpdating(systemConfiguration.getConfigurationData()).readValue(objectMapper.writeValueAsString(newConfigurationData));
            saveConfiguration();
        } catch (Exception e) {
            System.out.println("Failed to update data");
        }
        return this.systemConfiguration;
    }
}
