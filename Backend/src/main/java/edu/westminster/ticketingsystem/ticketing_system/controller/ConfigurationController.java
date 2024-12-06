package edu.westminster.ticketingsystem.ticketing_system.controller;
import edu.westminster.ticketingsystem.ticketing_system.config.SystemConfiguration;
import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import edu.westminster.ticketingsystem.ticketing_system.service.ConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PutMapping
    public SystemConfiguration updateSystemConfigData(@RequestBody ConfigurationData newConfigurationData) {
        return configurationService.updateSystemConfigData(newConfigurationData);
    }

    @GetMapping("/status")
    public boolean getSystemStatus() {
        return configurationService.getSystemConfigStatus();
    }

}
