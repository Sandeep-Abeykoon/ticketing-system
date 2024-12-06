package edu.westminster.ticketingsystem.ticketing_system.config;
import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SystemConfiguration {
    private final ConfigurationData configurationData;
    private boolean isSystemConfigured;
}
