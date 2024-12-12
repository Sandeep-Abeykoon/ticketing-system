# Ticketing System

A Spring Boot-based real-time event ticketing system that utilizes multi-threading and the producer-consumer pattern.

## Author
**Sandeep Chanura Abeykoon**  
Email: sandeepchanura@gmail.com

---

## Features
- Real-time ticket management for vendors, customers, and VIP customers.
- WebSocket integration for real-time logs and simulation status updates.
- Multi-threaded handling of participants using the producer-consumer pattern.
- In-memory H2 database with JPA for transaction logging.
- Configurable ticketing parameters through REST endpoints.

---

## Prerequisites
- **Java 21** or later.
- **Maven** for dependency management.

---

## Setup Instructions

### Clone the Repository
```bash
$ git clone <repository_url>
$ cd ticketing-system
```

### Build the Project
```bash
$ mvn clean install
```

### Run the Application
```bash
$ mvn spring-boot:run
```

---

## Configuration

### Application Properties
The `application.properties` file is configured as follows:

```properties
spring.application.name=Ticketing-system

# H2 database configuration
spring.datasource.url=jdbc:h2:mem:ticketing_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.hibernate.ddl-auto=update
```

### H2 Database Console
- Access the H2 Console at: `http://localhost:8080/h2-console`
- Use the following credentials:
    - **JDBC URL**: `jdbc:h2:mem:ticketing_db`
    - **Username**: `sa`
    - **Password**: `password`

---

## Usage

### CLI Commands
You can interact with the system using REST API endpoints. Use tools like `curl`, `Postman`, or your browser for testing.

#### Start Simulation
```bash
POST /api/simulation/start

Parameters:
- numberOfVendors: int
- numberOfCustomers: int
- numberOfVIPCustomers: int
```

#### Stop Simulation
```bash
POST /api/simulation/stop
```

#### Reset Simulation
```bash
POST /api/simulation/reset
```

#### Retrieve Simulation Status
```bash
GET /api/simulation/status
```

#### Add Participant
- Add Vendor:
  ```bash
  POST /api/participants/vendor/add
  ```

- Add Customer:
  ```bash
  POST /api/participants/customer/add?isVIP=false
  ```

- Add VIP Customer:
  ```bash
  POST /api/participants/customer/add?isVIP=true
  ```

#### Remove Participant
- Remove Vendor:
  ```bash
  DELETE /api/participants/vendor/remove/{vendorId}
  ```

- Remove Customer:
  ```bash
  DELETE /api/participants/customer/remove/{customerId}?isVIP=false
  ```

- Remove VIP Customer:
  ```bash
  DELETE /api/participants/customer/remove/{customerId}?isVIP=true
  ```

#### Retrieve Logs
```bash
GET /api/transactions
```

### GUI Usage
Access the WebSocket real-time dashboard:
1. Start the application.
2. Open `http://localhost:8080` in your browser.
3. View real-time logs and simulation status.

---

## Troubleshooting

### Common Issues

1. **Application Doesn't Start:**
    - Ensure Java 21 or later is installed.
    - Check if the port `8080` is free.

2. **Database Console Not Accessible:**
    - Verify `spring.h2.console.enabled=true` in `application.properties`.
    - Ensure the application is running.

3. **Real-Time Logs Not Updating:**
    - Check if WebSocket is enabled and configured correctly.
    - Verify browser compatibility for WebSocket.

4. **Simulation Not Starting:**
    - Ensure the system is configured via `/api/configuration` endpoints.
    - Verify that required parameters (vendors, customers) are provided.

### Logs
Logs are available in the console and the in-memory H2 database under the `Transaction` table. Use the H2 Console to query logs.

---

## Dependencies

The project uses the following dependencies:

- **Spring Boot Starter Web**: For RESTful web services.
- **Spring Boot Starter WebSocket**: For real-time communication.
- **Spring Boot Starter Data JPA**: For database interactions.
- **H2 Database**: In-memory database for storing transactions.
- **Lombok**: Reduces boilerplate code.
- **Spring Boot Starter Test**: For unit testing.

---

## License
This project is licensed under the MIT License.

