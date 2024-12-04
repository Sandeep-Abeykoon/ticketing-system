package edu.westminster.ticketingsystem.ticketing_system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileService {

    private static final String CONFIG_FILE = "src/main/java/edu/westminster/ticketingsystem/ticketing_system/config/systemConfig.json";

    private final ObjectMapper objectMapper;

    public FileService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Read configuration JSON and update the given ConfigurationData object
    public boolean readConfiguration(ConfigurationData configurationData) {
        File file = new File(CONFIG_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Configuration file is missing or empty.");
            return false;
        }

        try (FileReader reader = new FileReader(file)) {
            objectMapper.readerForUpdating(configurationData).readValue(reader);
            return true;
        } catch (IOException e) {
            System.out.println("Failed to read configuration file: " + e.getMessage());
            return false;
        }
    }

    // Write ConfigurationData object to JSON
    public boolean writeConfiguration(ConfigurationData configurationData) {
        File file = new File(CONFIG_FILE);

        try (FileWriter writer = new FileWriter(file)) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, configurationData);
            return true;
        } catch (IOException e) {
            System.out.println("Failed to save configuration file: " + e.getMessage());
            return false;
        }
    }
}
