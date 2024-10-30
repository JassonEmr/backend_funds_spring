package com.proyecto.valores.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection ="funds")
public class Fund {

    @Id
    private String id;
    private String name;
    private String category;
    private double minumSubscription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getMinumSubscription() {
        return minumSubscription;
    }

    public void setMinumSubscription(double minumSubscription) {
        this.minumSubscription = minumSubscription;
    }
    
}
