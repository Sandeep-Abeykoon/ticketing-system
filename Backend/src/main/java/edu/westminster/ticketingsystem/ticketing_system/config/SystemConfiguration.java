package edu.westminster.ticketingsystem.ticketing_system.config;

import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * SystemConfiguration class manages the configuration data for the ticketing system.
 * It encapsulates the system's configuration settings and provides a flag to track
 * whether the system is fully configured.
 */
@Component
@Data
public class SystemConfiguration {

    /**
     * Holds the configuration data for the ticketing system.
     */
    private final ConfigurationData configurationData;

    /**
     * Flag indicating whether the system has been fully configured.
     */
    private boolean isSystemConfigured;
}
