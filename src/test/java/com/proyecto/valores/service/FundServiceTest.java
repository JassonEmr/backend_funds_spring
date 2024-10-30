package com.proyecto.valores.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.proyecto.valores.model.Fund;
import com.proyecto.valores.model.Transaction;
import com.proyecto.valores.model.User;
import com.proyecto.valores.repository.FundRespository;
import com.proyecto.valores.repository.TransactionRepository;
import com.proyecto.valores.repository.UserRepository;

public class FundServiceTest {

    @Mock
    private FundRespository fundRespository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FundService fundService;

    private User user;
    private Fund fund;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId("671fb09a6dbf7714d23ece62");
        user.setEmail("jgualguanguzman@gmail.com");
        user.setBalance(1000);
        
        fund = new Fund();
        fund.setId("671ba9b679574c2da43debd0");
        fund.setName("FPV_BTG_PACTUAL_DINAMICA");
        fund.setMinumSubscription(100000);
        
        transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setUserId(user.getId());
    }

    @Test
    void testCreateFund() {
        when(fundRespository.save(fund)).thenReturn(fund);

        Fund createdFund = fundService.createFund(fund);

        assertEquals("FPV_BTG_PACTUAL_DINAMICA", createdFund.getName());
    }

    @SuppressWarnings("null")
    @Test
    void testSubscribeToFund_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fundRespository.findById(fund.getId())).thenReturn(Optional.of(fund));
        when(transactionRepository.findByUserIdAndFundId(user.getId(), fund.getId())).thenReturn(new ArrayList<>());

        ResponseEntity<Map<String, String>> response = fundService.subscribeToFound(user.getId(), fund.getId());

        assertEquals("Suscripcion exitosa al fondo Fund Example", response.getBody().get("message"));
        assertEquals("registered", response.getBody().get("status"));
        assertEquals(100000, user.getBalance()); // Balance after subscription
    }

    @SuppressWarnings("null")
    @Test
    void testCancelSubscription_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fundRespository.findById(fund.getId())).thenReturn(Optional.of(fund));

        ResponseEntity<Map<String, String>> response = fundService.cancelSubscription(user.getId(), fund.getId());

        assertEquals("Cancelación exitosa del fondo Fund Example", response.getBody().get("message"));
        assertEquals("finished", response.getBody().get("status"));
        assertEquals(1000, user.getBalance()); // Balance after cancellation
    }
     
}
