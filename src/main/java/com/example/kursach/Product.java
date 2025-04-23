package com.example.kursach;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private String description;
    private String imagePath;

    public Product(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}