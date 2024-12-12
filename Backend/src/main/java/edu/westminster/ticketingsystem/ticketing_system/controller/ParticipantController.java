package edu.westminster.ticketingsystem.ticketing_system.controller;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ParticipantController provides REST endpoints for managing participants in the simulation.
 * This controller handles operations such as adding and removing vendors and customers (normal or VIP).
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/participants")
public class ParticipantController {

    private final SimulationService simulationService;

    /**
     * Adds a new vendor to the simulation.
     *
     * @return ResponseEntity with a success message or error message if the operation fails.
     */
    @PostMapping("/vendor/add")
    public ResponseEntity<?> addVendor() {
        try {
            simulationService.addVendor();
            return ResponseEntity.ok("Vendor added successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to add vendor: " + e.getMessage());
        }
    }

    /**
     * Removes a vendor from the simulation.
     *
     * @param vendorId The ID of the vendor to be removed.
     * @return ResponseEntity with a success message or error message if the operation fails.
     */
    @DeleteMapping("/vendor/remove/{vendorId}")
    public ResponseEntity<?> removeVendor(@PathVariable String vendorId) {
        try {
            simulationService.removeVendor(vendorId);
            return ResponseEntity.ok("Vendor removed successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to remove vendor: " + e.getMessage());
        }
    }

    /**
     * Adds a new customer to the simulation.
     *
     * @param isVIP Indicates whether the customer is a VIP.
     * @return ResponseEntity with a success message or error message if the operation fails.
     */
    @PostMapping("/customer/add")
    public ResponseEntity<?> addCustomer(@RequestParam boolean isVIP) {
        try {
            simulationService.addCustomer(isVIP);
            return ResponseEntity.ok((isVIP ? "VIP Customer" : "Customer") + " added successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to add customer: " + e.getMessage());
        }
    }

    /**
     * Removes a customer from the simulation.
     *
     * @param customerId The ID of the customer to be removed.
     * @param isVIP Indicates whether the customer is a VIP.
     * @return ResponseEntity with a success message or error message if the operation fails.
     */
    @DeleteMapping("/customer/remove/{customerId}")
    public ResponseEntity<?> removeCustomer(
            @PathVariable String customerId, // Assuming the customer id can be any String although for now only plain numbers are used.
            @RequestParam boolean isVIP) {
        try {
            simulationService.removeCustomer(customerId, isVIP);
            return ResponseEntity.ok((isVIP ? "VIP Customer" : "Customer") + " removed successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to remove customer: " + e.getMessage());
        }
    }
}
