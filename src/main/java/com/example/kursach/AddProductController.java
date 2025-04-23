package com.example.kursach;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class AddProductController {

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField imagePathField;

    @FXML
    private void handleAddProduct() {
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        String imagePath = imagePathField.getText().trim();

        if (!name.isEmpty() && !description.isEmpty() && !imagePath.isEmpty()) {
            // Создание нового продукта
            Product newProduct = new Product(name, description, imagePath);
            addProductToDatabase(newProduct); // Добавление товара в БД

            // Закрытие окна
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close(); // Закрываем форму
        } else {
            // Обработка случаев, когда поля не заполнены
            System.out.println("Пожалуйста, заполните все поля.");
        }
    }


    private void addProductToDatabase(Product product) {
        String url = "jdbc:mysql://localhost:3306/ServerJava"; // URL вашей БД
        String user = "root"; // Укажите имя пользователя
        String password = "root"; // Укажите пароль

        String query = "INSERT INTO products (name, description, image_path) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setString(3, product.getImagePath());

            pstmt.executeUpdate();
            System.out.println("Продукт успешно добавлен в базу данных.");


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при добавлении продукта в базу данных.");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close(); // Закрытие окна без добавления товара
    }
}