package com.proyecto.valores.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.proyecto.valores.model.User;
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
