export const createStateMapper = (setters) => {
    return {
      isRunning: setters.setSimulationStatus,
      ticketCount: setters.setAvailableTickets,
      totalTicketsAdded: setters.setTotalTicketsAdded,
      totalTicketsRetrieved: setters.setTotalTicketsRetrieved,
      totalVIPRetrievals: setters.setTotalVIPRetrievals,
      totalNormalRetrievals: setters.setTotalNormalRetrievals,
      numberOfCustomers: setters.setNumberOfCustomers,
      numberOfVendors: setters.setNumberOfVendors,
      numberOfVIPCustomers: setters.setNumberOfVIPCustomers,
    };
  };
  
  export const updateStateFromResponse = (response, stateMapper, setLogs) => {
    Object.entries(stateMapper).forEach(([key, setter]) => {
      if (key in response) {
        setter(response[key] || 0); // Set state or default to 0
      }
    });
    if (setLogs) {
      setLogs(response.logs || []); // Handle logs separately
    }
  };
  