package com.proyecto.valores.repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.proyecto.valores.model.Fund;

public interface FundRespository extends MongoRepository<Fund, String> {
}
