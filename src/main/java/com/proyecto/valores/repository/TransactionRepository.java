package com.proyecto.valores.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.proyecto.valores.model.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByUserId(String userId);

    List<Transaction> findByUserIdAndTransactionType(String userId, String transactionType);

    List<Transaction> findByUserIdAndFundId(String userId, String fundId);

    List<Transaction> findByUserIdAndTransactionTypeAndStatus(String userId, String transactionType, String status);
}
