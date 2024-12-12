package edu.westminster.ticketingsystem.ticketing_system.repository;

import edu.westminster.ticketingsystem.ticketing_system.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TransactionRepository provides CRUD operations for the Transaction entity.
 * This repository interface extends JpaRepository, allowing access to built-in
 * database operations such as save, delete, and find.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
