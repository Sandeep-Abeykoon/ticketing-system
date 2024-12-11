package edu.westminster.ticketingsystem.ticketing_system.service;

import edu.westminster.ticketingsystem.ticketing_system.model.Transaction;
import edu.westminster.ticketingsystem.ticketing_system.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

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

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
