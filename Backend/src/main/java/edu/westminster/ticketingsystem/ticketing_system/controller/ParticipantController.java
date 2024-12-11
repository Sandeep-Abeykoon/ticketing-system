package edu.westminster.ticketingsystem.ticketing_system.controller;

import edu.westminster.ticketingsystem.ticketing_system.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/participants")
public class ParticipantController {

    private final SimulationService simulationService;

    // Add a vendor
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

    // Remove a vendor
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

    // Add a customer (Normal or VIP)
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

    // Remove a customer (Normal or VIP)
    @DeleteMapping("/customer/remove/{customerId}")
    public ResponseEntity<?> removeCustomer(
            @PathVariable String customerId,
            @RequestParam boolean isVIP) {
        try {
            System.out.println("The Id is" + customerId);
            simulationService.removeCustomer(customerId, isVIP);
            return ResponseEntity.ok((isVIP ? "VIP Customer" : "Customer") + " removed successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to remove customer: " + e.getMessage());
        }
    }
}
