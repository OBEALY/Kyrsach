package com.example.kursach;

public class ConcreteProductFactory implements ProductFactory {
    @Override
    public Product createProduct(String name, String description, String imagePath) {
        return new Product(name, description, imagePath);
    }
}