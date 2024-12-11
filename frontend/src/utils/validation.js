export const validateField = (name, value) => {
  if (isNaN(value)) {
    return "Value must be numeric";
  }

  if (value < 0) {
    return "Value cannot be negative";
  }

  if (name === "numberOfVIPCustomers") {
    return null;
  }

  if (value <= 0) {
    return `${name.replace(/([A-Z])/g, " $1")} must be greater than 0`;
  }

  return null;
};
