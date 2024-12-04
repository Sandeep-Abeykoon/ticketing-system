package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    private final FileService fileService;
    private final SystemConfiguration systemConfiguration;

    public ConfigurationService(FileService fileService, SystemConfiguration systemConfiguration) {
        this.fileService = fileService;
        this.systemConfiguration = systemConfiguration;
    }

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
}
