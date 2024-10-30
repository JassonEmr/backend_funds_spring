package com.proyecto.valores.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.proyecto.valores.model.Fund;
import com.proyecto.valores.model.Transaction;
import com.proyecto.valores.model.User;
import com.proyecto.valores.repository.FundRespository;
import com.proyecto.valores.repository.TransactionRepository;
import com.proyecto.valores.repository.UserRepository;

@Service
public class FundService {

    @Autowired
    private FundRespository fundRespository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public Fund createFund(Fund fund) {
        return fundRespository.save(fund);
    }

    public Optional<Fund> getFundById(String id) {
        return fundRespository.findById(id);
    }

    public boolean isUserSubscribedToFund(String userId, String fundId) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndFundId(userId, fundId);
        return !transactions.isEmpty();
    }

    public List<Fund> getSubscribeFunds(String userId) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndTransactionType(userId, "subscription");
        List<Fund> subscribeFunds = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if ("registered".equals(transaction.getStatus())) {
                Fund fund = fundRespository.findById(transaction.getFundId())
                        .orElseThrow(() -> new RuntimeException("Fondo no encontrado"));
                subscribeFunds.add(fund);
            }
        }

        return subscribeFunds;
    }

    public ResponseEntity<Map<String, String>> subscribeToFound(String userId, String fundId) {
        Map<String, String> response = new HashMap<>();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Fund fund = fundRespository.findById(fundId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        /* Verificar si el usuario esta en el fondo */
        if (isUserSubscribedToFund(userId, fundId)) {
            response.put("message",
                    "Ya estas suscrito al fondo " + fund.getName() + ". Cancela la suscripción para volver a unirte.");
            return ResponseEntity.badRequest().body(response);
        }

        if (user.getBalance() < fund.getMinumSubscription()) {
            response.put("message", "No tiene saldo disponible para vincularse al fondo " + fund.getName());
            return ResponseEntity.badRequest().body(response);
        }

        user.setBalance(user.getBalance() - fund.getMinumSubscription());
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setFundId(fund.getId());
        transaction.setFundName(fund.getName());
        transaction.setTransactionType("subscription");
        transaction.setAmount(fund.getMinumSubscription());
        transaction.setDate(LocalDateTime.now());
        transaction.setUserId(user.getId());
        transaction.setStatus("registered");

        transactionRepository.save(transaction);

        response.put("message", "Suscripcion exitosa al fondo " + fund.getName());
        response.put("fundName", fund.getName());
        response.put("transactionId", transaction.getId());
        response.put("userId", user.getId());
        response.put("status", transaction.getStatus());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> cancelSubscription(String userId, String fundId) {
        Map<String, String> response = new HashMap<>();

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Fund fund = fundRespository.findById(fundId).orElseThrow(() -> new RuntimeException("Fondo no encontrado"));
        user.setBalance(user.getBalance() + fund.getMinumSubscription());
        userRepository.save(user);
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setFundId(fund.getId());
        transaction.setFundName(fund.getName());
        transaction.setTransactionType("cancellation");
        transaction.setAmount(fund.getMinumSubscription());
        transaction.setDate(LocalDateTime.now());
        transaction.setUserId(user.getId());
        transaction.setStatus("finished");

        transactionRepository.save(transaction);

        response.put("message", "Cancelación exitosa del fondo " + fund.getName());
        response.put("fundName", fund.getName());
        response.put("transactionId", transaction.getId());
        response.put("userId", user.getId());
        response.put("status", transaction.getStatus());

        return ResponseEntity.ok(response);
    }

}
