# Ticket Management System

This project is a console-based application for managing ticket operations. The system simulates vendors adding tickets to a pool and customers retrieving them, demonstrating concurrency in a shared resource environment.

## Features

- **Concurrent Simulation**:
    - Vendors and customers operate simultaneously.
- **Configurable Parameters**:
    - Customize ticket release and retrieval rates, intervals, and pool capacity.
- **Simulation Summary**:
    - Provides statistics on ticket operations after simulation ends.
- **Persistence**:
    - Configuration settings are saved and loaded automatically.

## Setup Instructions

### Clone the Repository:

```bash
git clone https://github.com/your-repository/ticket-management-system.git
cd ticket-management-system
```

### Compile the Application:
Ensure you have Java installed (version 8 or higher).

```bash
javac -d bin src/**/*.java
```

### Run the Application:

```bash
java -cp bin main.Main
```

## Usage Guidelines

### 1. Start the Application

When you run the application, you'll see the main menu with these options:

```
=======================================
     TICKET MANAGEMENT SYSTEM
=======================================

Current Configuration:
<Displays current settings or prompts to create a new configuration>

Menu Options:
1. Edit Configuration
2. Start Simulation
3. Exit
```

### 2. Edit Configuration

- Select `1` to update system settings.
- You'll be prompted to provide the following:
    - **Total Tickets**: Initial number of tickets.
    - **Ticket Release Rate**: Number of tickets released per interval.
    - **Ticket Release Interval**: Time (ms) between ticket releases.
    - **Customer Retrieval Rate**: Number of tickets customers retrieve per interval.
    - **Ticket Retrieval Interval**: Time (ms) between retrievals.
    - **Maximum Pool Capacity**: Maximum number of tickets in the pool.

### 3. Start Simulation

- Select `2` to begin the simulation.

- Provide:
    - **Number of Vendors**: Vendors adding tickets to the pool.
    - **Number of Customers**: Customers retrieving tickets from the pool.

- The simulation runs until you press `Enter` to stop. During the simulation:
    - Vendors and customers operate concurrently.
    - Real-time logs display their actions.

### 4. Exit

- Select `3` to exit the application.

## Simulation Summary

After stopping the simulation, you'll see a summary:
- **Total Tickets in Pool**: Remaining tickets.
- **Total Tickets Added**: Cumulative tickets added by vendors.
- **Total Tickets Retrieved**: Cumulative tickets retrieved by customers.

## Troubleshooting

### 1. Java Not Found
Ensure Java is installed and added to your system's PATH. Verify with:

```bash
java -version
```

### 2. Configuration File Issues
If you encounter errors loading the configuration:
- Check if `config.ser` exists in the project directory.
- If corrupted, delete `config.ser` and restart the application to create a new configuration.

### 3. Thread Interruption Issues
If the application hangs when stopping the simulation:
- Press `Ctrl+C` to terminate the program.
- Restart and try again.

## Project Structure

```
src/
├── Configuration.java
├── Customer.java
├── InputValidator.java
├── Main.java
├── Person.java
├── SimulationManager.java
├── TicketPool.java
├── Vendor.java
└── ConfigurationManager.java
```

## Contributing

Feel free to fork this repository and submit pull requests for improvements.

---

**Author**: Sandeep Chanura Abeykoon  
**Email**: sandeepchanura@gmail.com

