package com.proyecto.valores.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.proyecto.valores.model.Fund;
import com.proyecto.valores.model.User;
import com.proyecto.valores.repository.FundRespository;
import com.proyecto.valores.repository.UserRepository;
import com.proyecto.valores.service.FundService;

@WebMvcTest(FundController.class)
public class FundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FundService fundService;

    @Mock
    private FundRespository fundRespository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FundController fundController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fundController).build();
    }

    @Test
    void testGetAllFunds() throws Exception {
        when(this.fundRespository.findAll()).thenReturn(Collections.singletonList(new Fund()));

        mockMvc.perform(get("/api/funds/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetFundById() throws Exception {
        String fundId = "671ba9b679574c2da43debd0";
        Fund fund = new Fund();
        when(this.fundService.getFundById(fundId)).thenReturn(Optional.of(fund));

        mockMvc.perform(get("/api/funds/{id}", fundId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testCreateFund() throws Exception {
        Fund fund = new Fund();
        when(this.fundService.createFund(any(Fund.class))).thenReturn(fund);

        mockMvc.perform(post("/api/funds/create")
                        .contentType("application/json")
                        .content("{\"name\":\"FPV_BTG_PACTUAL_DINAMICA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void testSubscribeToFund() throws Exception {
        String userId = "671fb09a6dbf7714d23ece62";
        String fundId = "671ba9b679574c2da43debd0";
        User user = new User(); // Agrega los campos necesarios a este usuario
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Suscripcion exitosa");
        ResponseEntity<Map<String, String>> responseEntity = ResponseEntity.ok(responseMap);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.fundService.subscribeToFound(userId, fundId)).thenReturn(responseEntity);

        mockMvc.perform(post("/api/funds/subscribe/{userId}/{fundId}", userId, fundId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Suscripcion exitosa"));
    }

    @Test
    void testCancelSubscription() throws Exception {
        String userId = "671fb09a6dbf7714d23ece62";
        String fundId = "671ba9b679574c2da43debd0";
        User user = new User();
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Cancelación exitosa");
        ResponseEntity<Map<String, String>> responseEntity = ResponseEntity.ok(responseMap);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.fundService.cancelSubscription(userId, fundId)).thenReturn(responseEntity);

        mockMvc.perform(post("/api/funds/cancel/{userId}/{fundId}", userId, fundId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cancelación exitosa"));
    }

    @Test
    void testGetUserSubscribeFunds() throws Exception {
        String userId = "671fb09a6dbf7714d23ece62";
        when(this.fundService.getSubscribeFunds(userId)).thenReturn(Collections.singletonList(new Fund()));

        mockMvc.perform(get("/api/funds/user/{userId}/subscribe-funds", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
     
}
