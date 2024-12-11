package edu.westminster.ticketingsystem.ticketing_system.repository;

import edu.westminster.ticketingsystem.ticketing_system.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(String userId);
}
