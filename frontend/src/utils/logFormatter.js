export const formatLogMessage = (log) => {
    const { timestamp, action, details } = log;
  
    let message = "";
    let color = "black";
  
    // Skip processing and logging for actions that don't need to be shown
    if (action === "TICKET_ADD" || action === "USER_UPDATE") {
        return null; 
    }
  
    switch (action) {
        case "TICKET_ADD_VENDOR":
            message = `${timestamp} - Vendor (${details.id}) added ${details.ticketsAdded} tickets.`;
            color = "black";
            break;

        case "CUSTOMER_ADDED":
            message = `${timestamp} - ${details.type} (${details.id}) added.`;
            color = details.type === "VIP Customer" ? "#B8860B" : "green";
            break;

        case "CUSTOMER_REMOVED":
            message = `${timestamp} - Customer (${details.id}) removed.`;
            color = "red";
            break;

        case "VENDOR_ADDED":
            message = `${timestamp} - Vendor (${details.id}) added.`;
            color = "green";
            break;

        case "VENDOR_REMOVED":
            message = `${timestamp} - Vendor (${details.id}) removed.`;
            color = "red";
            break;
    
        case "TICKET_RETRIEVAL":
            message = `${timestamp} - ${details.customerType} Customer (${details.customerId}) retrieved ${details.retrievedTickets} tickets.`;
            color = details.customerType === "VIP" ? "#B8860B" : "black";
            break;
    
        case "TICKET_RETRIEVAL_FAILED":
            message = `${timestamp} - ${details.customerType} Customer (${details.customerId}) failed to retrieve tickets. Reason: ${details.reason}.`;
            color = "red";
            break;
    
        case "POOL_CLEARED":
            message = `${timestamp} - Ticket pool cleared.`;
            color = "black";
            break;
    
        case "SIMULATION_STARTED":
            message = `${timestamp} - Simulation started with ${details.numberOfCustomers} customers, ${details.numberOfVIPCustomers} VIP customers, and ${details.numberOfVendors} vendors.`;
            color = "blue";
            break;
    
        case "SIMULATION_STOPPED":
            message = `${timestamp} - Simulation stopped.`;
            color = "blue";
            break;
    
        case "THREAD_STARTED":
            message = `${timestamp} - ${details.type} (${details.id}) thread started.`;
            color = "black";
            break;

        case "THREAD_INTERRUPTED":
            message = `${timestamp} - ${details.type} (${details.id}) thread interrupted.`;
            color = "black";
            break;
    
        case "TICKET_ADD_FAILED":
            message = `${timestamp} - Vendor (${details.id}) failed to add tickets (${details.ticketsPerRelease}). Reason: ${details.reason}.`;
            color = "red";
            break;

        case "PARTICIPANT_ERROR":
            message = `${timestamp} - Vendor (${details.id}) encountered an error. Reason: ${details.reason}.`;
            color = "red";
            break;
    
        default:
            message = `${timestamp} - Unknown action: ${action}.`;
            color = "black";
            break;
    }
  
    return { message, color };
};
