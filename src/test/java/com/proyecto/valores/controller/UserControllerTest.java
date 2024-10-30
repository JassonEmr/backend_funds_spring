package com.proyecto.valores.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.valores.model.Fund;
import com.proyecto.valores.model.Transaction;
import com.proyecto.valores.model.User;
import com.proyecto.valores.repository.FundRespository;
import com.proyecto.valores.repository.TransactionRepository;
import com.proyecto.valores.repository.UserRepository;
import com.proyecto.valores.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private FundRespository fundRespository;

    @InjectMocks
    private UserController userController;

    private User user;
    private List<User> users;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("jgualguanguzman@gmail.com");
        user.setName("Jasson Esteban Gualguan Guzman");
        users = new ArrayList<>();
        users.add(user);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("jgualguanguzman@gmail.com"))
                .andExpect(jsonPath("$[0].name").value("Jasson Esteban Gualguan Guzman"));
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jgualguanguzman@gmail.com"))
                .andExpect(jsonPath("$.name").value("Jasson Esteban Gualguan Guzman"));
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        when(userService.findByUserEmail("jgualguanguzman@gmail.com")).thenReturn(user);

        mockMvc.perform(get("/api/users/existUser/{email}", "jgualguanguzman@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jgualguanguzman@gmail.com"))
                .andExpect(jsonPath("$.name").value("Jasson Esteban Gualguan Guzman"));
    }

    @Test
    public void testGetFundsByUser() throws Exception {
        List<Fund> funds = new ArrayList<>();
        Fund fund = new Fund();
        funds.add(fund);

        when(transactionRepository.findByUserIdAndTransactionType("671fb09a6dbf7714d23ece62", "subscription")).thenReturn(new ArrayList<>());
        when(fundRespository.findAllById(any())).thenReturn(funds);

        mockMvc.perform(get("/api/users/funds/{userId}", "671fb09a6dbf7714d23ece62"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetTransactionsByUser() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        when(transactionRepository.findByUserId("671fb09a6dbf7714d23ece62")).thenReturn(transactions);

        mockMvc.perform(get("/api/users/transactions/{UserId}", "671fb09a6dbf7714d23ece62"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetAllTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        when(transactionRepository.findAll()).thenReturn(transactions);

        mockMvc.perform(get("/api/users/getTransactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

}
