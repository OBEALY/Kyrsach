package com.example.kursach;

public interface ProductFactory {
    Product createProduct(String name, String description, String imagePath);
}