package edu.westminster.ticketingsystem.ticketing_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Ticketing System application.
 * This class initializes and starts the Spring Boot application.
 */
@SpringBootApplication
public class TicketingSystemApplication {

	/**
	 * The main method to launch the Spring Boot application.
	 *
	 * @param args Command-line arguments passed during the application startup.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TicketingSystemApplication.class, args);
	}
}
