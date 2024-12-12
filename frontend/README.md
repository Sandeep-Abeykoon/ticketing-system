# Ticketing System Application

## Overview
This project is a **Ticketing System Application** built using React and Material-UI for the frontend, along with a WebSocket integration for real-time data updates. It features a simulation of ticketing operations with support for managing participants (vendors, normal customers, and VIP customers), analyzing transaction data, and visualizing analytics through various charts.

## Features

### Main Features
- **Real-Time Updates**: Leverages WebSocket for live updates on ticket availability, sales, and participant management.
- **Responsive UI**: Optimized for both desktop and mobile devices using Material-UI's responsive components.
- **Simulation Dashboard**: Start, stop, and monitor ticketing simulations with real-time logs.
- **Analytics Dashboard**: Visualize sales data with line charts, bar charts, and pie charts.
- **Configuration Page**: Manage system settings such as ticket release rates and capacity.
- **Transactions Overview**: View a detailed list of all transactions, including ticket sales and additions.
- **User Management**: Add or remove vendors and customers dynamically.

### Pages
1. **Header Navigation**: Centralized navigation for Configuration, Simulation, Dashboard, Transactions, and User Management.
2. **Configuration Page**: Configure system parameters and update settings with form validation.
3. **Simulation Page**: Start or stop simulations, monitor logs, and reset data.
4. **Analytics Dashboard**: Real-time visual analytics of ticket sales and participant data.
5. **Transactions Page**: Detailed transaction history with aggregate statistics.
6. **User Management Page**: Manage participants dynamically with user-friendly controls.

## Technologies Used
- **Frontend**:
  - React (Functional Components, Context API)
  - Material-UI (for styling and components)
  - Chart.js (for data visualization)
- **Backend**:
  - REST API for system configuration and participant management
  - WebSocket for real-time updates
  - Axios for HTTP requests
- **Utilities**:
  - Validation Functions
  - Log Formatter
  - State Mapper for efficient state management

## Installation and Setup

### Prerequisites
- Node.js (v14 or higher)
- npm or yarn
- Backend server running at `http://localhost:8080`

### Steps
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the application:
   ```bash
   npm start
   ```
4. Access the application at `http://localhost:3000`.

### Backend Configuration
Ensure the backend server is running with the following endpoints:
- `/api/configuration` for fetching and updating configurations.
- `/api/simulation` for starting, stopping, and resetting simulations.
- `/api/transactions` for retrieving transaction data.
- `/participants` for managing vendors and customers.

## Directory Structure
```
src
|-- components
|   |-- Header.jsx          # Navigation bar
|   |-- ConfigurationPage.jsx  # Configuration management
|   |-- SimulationPage.jsx  # Simulation controls and logs
|   |-- AnalyticsDashboard.jsx # Charts and analytics
|   |-- TransactionsPage.jsx   # Transaction history
|   |-- UserManagementPage.jsx # Add/remove vendors and customers
|
|-- context
|   |-- WebSocketContext.jsx   # WebSocket state management
|
|-- utils
|   |-- validation.js       # Form validation
|   |-- logFormatter.js     # Format logs for display
|   |-- stateMapper.js      # Map API responses to state
|
|-- api
|   |-- api.js         #  API addresses for functional purposes
|
|-- App.js                  # Main application
|-- index.js                # Entry point
```

## Usage
### Simulation Management
- Navigate to the **Simulation Page** to start or stop the simulation.
- Adjust the number of customers, VIP customers, and vendors before starting.
- Monitor logs in real time.

### Analytics
- Access the **Analytics Dashboard** to view line, bar, and pie charts showing ticket sales trends.
- Analyze data for Normal and VIP customers.

### User Management
- Add or remove participants (vendors, normal customers, VIP customers) from the **User Management Page**.

### Configuration
- Update system parameters on the **Configuration Page** with real-time validation.

### Transactions
- View detailed transaction history and aggregate statistics on the **Transactions Page**.

## Author
**Sandeep Chanura Abeykoon**

**Email:** sandeepchanura@gmail.com

## License
This project is licensed under the MIT License. See `LICENSE` for more details.

