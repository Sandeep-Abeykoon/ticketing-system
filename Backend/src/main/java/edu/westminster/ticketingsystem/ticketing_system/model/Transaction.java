package edu.westminster.ticketingsystem.ticketing_system.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String userId;
    private String userType;
    private int ticketCount;
    private LocalDateTime timestamp;
    private String details;
}
