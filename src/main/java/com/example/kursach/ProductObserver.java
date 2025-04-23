package com.example.kursach;

public class ProductObserver implements Observer {
    private HelloController controller;

    public ProductObserver(HelloController controller) {
        this.controller = controller;
    }

    @Override
    public void update() {
        controller.loadProducts(); // Обновляем продукты в контроллере
    }
}