package com.proyecto.valores.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.proyecto.valores.model.Email;
import com.proyecto.valores.service.EmailService;

@WebMvcTest(EmailController.class)
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
    }

    @Test
    void testSendEmail() throws Exception {
        Email email = new Email();
        email.setTo("jgualguanguzman@gmail.com");
        email.setSubject("Test Subject");
        email.setText("This is a test email.");

        mockMvc.perform(post("/api/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"to\":\"jgualguanguzman@gmail.com\", \"subject\":\"Test Subject\", \"text\":\"This is a test email.\"}"))
                .andExpect(status().isOk());

        verify(emailService).sendEmail(email.getTo(), email.getSubject(), email.getText());
    }
    
}
