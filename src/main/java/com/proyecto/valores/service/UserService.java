package com.proyecto.valores.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.valores.model.User;
import com.proyecto.valores.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        user.setBalance(500000);
        return userRepository.save(user);
    }

    public User findByUserEmail(String email){
        return userRepository.findByEmail(email);
    }
}
