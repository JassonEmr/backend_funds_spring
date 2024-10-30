package com.proyecto.valores.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.proyecto.valores.model.User;
import com.proyecto.valores.repository.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("jgualguanguzman@gmail.com");
        user.setName("Jasson Gualguan");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals(500000, createdUser.getBalance());
        assertEquals("jgualguanguzman@gmail.com", createdUser.getEmail());
    }

    @Test
    void testFindByUserEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        User foundUser = userService.findByUserEmail(user.getEmail());

        assertEquals("jgualguanguzman@gmail.com", foundUser.getEmail());
        assertEquals("Jasson Gualguan", foundUser.getName());
    }
     
}
