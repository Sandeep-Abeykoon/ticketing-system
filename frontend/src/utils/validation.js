export const validateField = (name, value) => {
    if (name !== "totalTickets" && value <= 0) {
      return "Value must be greater than 0";
    }
    if (value < 0 || isNaN(value)) {
      return "Value cannot be negative or non-numeric";
    }
    return "";
  };
  