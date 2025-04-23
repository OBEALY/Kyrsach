package com.example.kursach;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class ProductClient {

    public static void main(String[] args) {
        new ProductClient().start(); // Запуск клиента
    }

    public void start() {
        // Вызов метода для получения продуктов и их вывод
        List<Product> products = fetchProducts("192.168.198.1", 12345);
        displayProducts(products);
    }

    public List<Product> fetchProducts(String serverAddress, int port) {
        try (Socket socket = new Socket(serverAddress, port);
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Приведение к типу List<Product>
            @SuppressWarnings("unchecked")
            List<Product> products = (List<Product>) in.readObject();

            return products; // Возврат списка продуктов

        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Класс не найден: " + e.getMessage());
        }

        return null; // Возврат null в случае ошибки
    }

    private void displayProducts(List<Product> products) {
        if (products != null) {
            for (Product product : products) {
                System.out.println("Название: " + product.getName());
                System.out.println("Описание: " + product.getDescription());
                System.out.println("Путь к изображению: " + product.getImagePath());
                System.out.println();
            }
        } else {
            System.out.println("Нет доступных продуктов.");
        }
    }
}