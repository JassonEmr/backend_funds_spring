package com.proyecto.valores.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.valores.model.Fund;
import com.proyecto.valores.model.Transaction;
import com.proyecto.valores.model.User;
import com.proyecto.valores.repository.FundRespository;
import com.proyecto.valores.repository.TransactionRepository;
import com.proyecto.valores.repository.UserRepository;
import com.proyecto.valores.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name="User")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FundRespository fundRespository;

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping("/get")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Operation(summary = "Obtener todas las transacciones")
    @GetMapping("/getTransactions")
    public List<Transaction>getAllTransactions() {
        return transactionRepository.findAll();
    }
    
    @Operation(summary = "Obtener todos los fondos de un usuario por id")
    @GetMapping("/funds/{userId}")
    public List<Fund> getFundsByUser(@PathVariable String userId) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndTransactionType(userId, "subscription");
        if(transactions.isEmpty()){
            System.out.println("No se encontraron transacciones para el usuario con ID: " + userId);
            return new ArrayList<>();
        }

        List<String> fundIds = transactions.stream().map(Transaction::getFundId).collect(Collectors.toList());

        return fundRespository.findAllById(fundIds);
    }

    @Operation(summary = "Obtener todas las transacciones de un usuario por id")
    @GetMapping("/transactions/{UserId}")
    public List<Transaction> getTransactionsByUser(@PathVariable String UserId) {
        return transactionRepository.findByUserId(UserId);
    }

    @Operation(summary = "Obtener usuario por email")
    @GetMapping("/existUser/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User exists = userService.findByUserEmail(email);
        return ResponseEntity.ok(exists);
    }
    
    @Operation(summary = "crear un usuario")
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
    
    
}
