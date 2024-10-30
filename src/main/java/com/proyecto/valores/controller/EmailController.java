package com.proyecto.valores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.valores.model.Email;
import com.proyecto.valores.service.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Email")
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Operation(summary = "Funcion que permite enviar un email")
    @PostMapping("/send")
    public String sendEmail(@RequestBody Email email) {
        emailService.sendEmail(email.getTo(), email.getSubject(), email.getText());
        return "Email enviado exitosamente";
    }
}
