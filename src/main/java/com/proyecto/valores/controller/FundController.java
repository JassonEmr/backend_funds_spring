package com.proyecto.valores.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.valores.model.Fund;
import com.proyecto.valores.model.User;
import com.proyecto.valores.repository.FundRespository;
import com.proyecto.valores.repository.UserRepository;
import com.proyecto.valores.service.EmailService;
import com.proyecto.valores.service.FundService;
import com.proyecto.valores.service.SMSservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Email")
@RestController
@RequestMapping("/api/funds")
public class FundController {
 
    @Autowired
    private FundService fundService;

    @Autowired
    private FundRespository fundRespository;

    @Autowired
    private UserRepository userRepository;

    private final EmailService emailService;
    private final SMSservice smsService;

    public FundController(EmailService emailService, SMSservice smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

   
    @Operation(summary = "Obtener todos los fondos")
    @GetMapping("/get")
    public List<Fund> getAllFunds() {
        return fundRespository.findAll();
    }

    @Operation(summary = "Obtener un fondo por id")
    @GetMapping("/{id}")
    public ResponseEntity<Fund> getFundById(@PathVariable String id) {
        return fundService.getFundById(id)
                .map(fund -> ResponseEntity.ok().body(fund))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Funcion que permite crear un fodo nuevo")
    @PostMapping("/create")
    public ResponseEntity<Fund> createFund(@RequestBody Fund fund) {
        Fund createFund = fundService.createFund(fund);
        return ResponseEntity.ok(createFund);
    }

    @Operation(summary = "Suscribirse a un fondo activo")
    @SuppressWarnings("null")
    @PostMapping("/subscribe/{userId}/{fundId}")
    public ResponseEntity<Map<String, String>> subscribeToFund(@PathVariable String userId, @PathVariable String fundId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ResponseEntity<Map<String, String>> response = fundService.subscribeToFound(userId, fundId);

        if ("Suscripcion exitosa".equals(response.getBody().get("message"))) {
            emailService.sendEmail(user.getEmail(), "Suscripción exitosa", response.getBody().get("message"));
            smsService.sendSms(user.getPhone(), response.getBody().get("message"));
        }
        return response;
    }

    @SuppressWarnings("null")
    @Operation(summary = "Funcion que permite cancelar la suscripcion a un fondo de un usuario")
    @PostMapping("/cancel/{userId}/{fundId}")
    public ResponseEntity<Map<String, String>> cancelSubscription(@PathVariable String userId, @PathVariable String fundId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        ResponseEntity<Map<String, String>> response = fundService.cancelSubscription(userId, fundId);

        if ("Cancelación exitosa".equals(response.getBody().get("message"))) {
            emailService.sendEmail(user.getEmail(), "Cancelación de suscripción", response.getBody().get("message"));
            smsService.sendSms(user.getPhone(), response.getBody().get("message"));
        }
        return response;
    }

    @Operation(summary = "Permite obtener los fondos inscritos de un usuario")
    @GetMapping("/user/{userId}/subscribe-funds")
    public ResponseEntity<List<Fund>> getUserSubscribeFunds(@PathVariable String userId) {
        List<Fund> funds = fundService.getSubscribeFunds(userId);
        return ResponseEntity.ok(funds);
    }

}
