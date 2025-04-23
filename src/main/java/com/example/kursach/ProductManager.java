package com.example.kursach;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private static ProductManager instance;
    private List<Product> allProducts = new ArrayList<>();

    private ProductManager() {}

    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public void setProducts(List<Product> products) {
        this.allProducts.clear();
        this.allProducts.addAll(products);
    }

    public List<Product> getProducts() {
        return allProducts;
    }

    public void addObserver(ProductObserver productObserver) {
    }
}