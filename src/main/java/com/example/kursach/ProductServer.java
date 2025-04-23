package com.example.kursach;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProductServer {
    private String user = "root"; // Ваше имя пользователя
    private String password = "root"; // Ваш пароль
    private List<Product> products = new ArrayList<>();
    private Timer timer;

    public static void main(String[] args) {
        new ProductServer().start(); // Запуск сервера
    }

    public ProductServer() {
        // Конструктор оставляем без изменений
    }

    public void start() {
        startUpdating(); // Запускаем обновление продуктов
        startServer();   // Запускаем сервер
    }

    private void startUpdating() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateProducts();
            }
        }, 0, 5000); // Обновление каждые 5 секунд
    }

    private void updateProducts() {
        // URL базы данных
        String url = "jdbc:mysql://localhost:3306/ServerJava";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement stmt = connection.createStatement()) {

            String query = "SELECT name, description, image_path FROM products";
            ResultSet rs = stmt.executeQuery(query);
            products.clear();

            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                String imagePath = rs.getString("image_path");
                products.add(new Product(name, description, imagePath));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Сервер запущен. Ожидание подключения...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клиент подключен.");

                new Thread(() -> {
                    try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
                        out.writeObject(products); // Отправка списка продуктов
                    } catch (IOException e) {
                        System.err.println("Ошибка при отправке данных клиенту: " + e.getMessage());
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            System.err.println("Ошибка при закрытии сокета: " + e.getMessage());
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopUpdating() {
        if (timer != null) {
            timer.cancel();
        }
    }
}