package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.model.Transaction;
import edu.westminster.ticketingsystem.ticketing_system.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TransactionService handles logging and retrieval of transactions in the ticketing system.
 * This service saves transaction details to the database and provides methods to access
 * transaction records.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Constructs a TransactionService with the given TransactionRepository.
     *
     * @param transactionRepository The repository used for transaction persistence.
     */
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Logs a new transaction in the system.
     *
     * @param type The type of transaction (e.g., "ADD", "RETRIEVE", "CLEAR").
     * @param userId The ID of the user associated with the transaction.
     * @param userType The type of user (e.g., "VENDOR", "VIP", "NORMAL").
     * @param ticketCount The number of tickets involved in the transaction.
     * @param details Additional details about the transaction.
     */
    public void logTransaction(
            String type, String userId, String userType, int ticketCount, String details) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setUserId(userId);
        transaction.setUserType(userType);
        transaction.setTicketCount(ticketCount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDetails(details);
        transactionRepository.save(transaction);
    }

    /**
     * Retrieves all transactions recorded in the system.
     *
     * @return A list of all transactions.
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
