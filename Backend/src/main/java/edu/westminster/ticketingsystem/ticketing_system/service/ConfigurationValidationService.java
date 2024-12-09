package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.model.ConfigurationData;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationValidationService {
    public void validateConfigurationData(ConfigurationData configData) {
        if (configData.getTicketReleaseRate() <= 0) {
            throw new IllegalArgumentException("Ticket release rate must be greater than 0");
        }
        if (configData.getTicketReleaseInterval() <= 0) {
            throw new IllegalArgumentException("Ticket release interval must be greater than 0");
        }
        if (configData.getCustomerRetrievalRate() <= 0) {
            throw new IllegalArgumentException("Customer retrieval rate must be greater than 0");
        }
        if (configData.getCustomerRetrievalInterval() <= 0) {
            throw new IllegalArgumentException("Customer retrieval interval must be greater than 0");
        }
        if (configData.getMaxTicketCapacity() <= 0) {
            throw new IllegalArgumentException("Maximum ticket capacity must be greater than 0");
        }
    }
}
