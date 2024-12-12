package edu.westminster.ticketingsystem.ticketing_system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * FileService handles reading and writing configuration data to a JSON file.
 * This service ensures that the system's configuration is persisted to a file and
 * can be reloaded when needed.
 */
@Service
@AllArgsConstructor
public class FileService {

    /**
     * Path to the configuration JSON file.
     */
    private static final String CONFIG_FILE = "src/main/java/edu/westminster/ticketingsystem/ticketing_system/config/systemConfig.json";

    private final ObjectMapper objectMapper;

    /**
     * Reads the configuration JSON file and updates the given ConfigurationData object.
     *
     * @param configurationData The ConfigurationData object to update with the file's contents.
     * @return true if the configuration was successfully read, false otherwise.
     */
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

    /**
     * Writes the given ConfigurationData object to a JSON file.
     *
     * @param configurationData The ConfigurationData object to save to the file.
     * @return true if the configuration was successfully written, false otherwise.
     */
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
