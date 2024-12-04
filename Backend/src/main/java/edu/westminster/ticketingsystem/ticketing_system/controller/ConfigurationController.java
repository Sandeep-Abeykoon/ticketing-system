package edu.westminster.ticketingsystem.ticketing_system.controller;
import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import edu.westminster.ticketingsystem.ticketing_system.service.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @GetMapping
    public SystemConfiguration getConfiguration() {
        return configurationService.getConfiguration();
    }

    @GetMapping("/status")
    public boolean getSystemStatus() {
        return configurationService.getSystemStatus();
    }

    @PutMapping
    public ConfigurationData updateSystemConfigData(@RequestBody ConfigurationData configurationData) {
        return configurationService.updateSystemConfigData(configurationData);
    }
}
