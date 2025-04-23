package com.example.kursach;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChangeProductController {

    @FXML
    private ListView<String> productListView; // Список для выбора продукта
    @FXML
    private TextField nameField; // Поле для изменения имени продукта
    @FXML
    private TextField descriptionField; // Поле для изменения описания продукта
    @FXML
    private TextField imagePathField; // Поле для изменения пути к изображению

    @FXML
    private void initialize() {
        loadProducts(); // Загружаем продукты при инициализации
    }

    private void loadProducts() {
        List<String> productNames = getProductNamesFromDatabase();
        productListView.getItems().addAll(productNames);
    }

    private List<String> getProductNamesFromDatabase() {
        List<String> productNames = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/ServerJava"; // URL вашей БД
        String user = "root"; // Укажите имя пользователя
        String password = "root"; // Укажите пароль

        String query = "SELECT name FROM products";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                productNames.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке продуктов из базы данных.");
        }
        return productNames;
    }

    @FXML
    private void handleProductSelection() {
        String selectedProduct = productListView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            loadProductDetails(selectedProduct);
        }
    }

    private void loadProductDetails(String productName) {
        String url = "jdbc:mysql://localhost:3306/ServerJava"; // URL вашей БД
        String user = "root"; // Укажите имя пользователя
        String password = "root"; // Укажите пароль

        String query = "SELECT description, image_path FROM products WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                descriptionField.setText(rs.getString("description"));
                imagePathField.setText(rs.getString("image_path"));
                nameField.setText(productName);
            } else {
                System.out.println("Продукт не найден.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке деталей продукта.");
        }
    }

    @FXML
    private void handleChangeProduct() {
        String selectedProduct = productListView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            String newName = nameField.getText().trim();
            String newDescription = descriptionField.getText().trim();
            String newImagePath = imagePathField.getText().trim();

            updateProductInDatabase(selectedProduct, newName, newDescription, newImagePath);
            Stage stage = (Stage) productListView.getScene().getWindow();
            stage.close(); // Закрываем форму
        } else {
            System.out.println("Пожалуйста, выберите продукт для изменения.");
        }
    }

    private void updateProductInDatabase(String oldName, String newName, String newDescription, String newImagePath) {
        String url = "jdbc:mysql://localhost:3306/ServerJava"; // URL вашей БД
        String user = "root"; // Укажите имя пользователя
        String password = "root"; // Укажите пароль

        String query = "UPDATE products SET name = ?, description = ?, image_path = ? WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newName);
            pstmt.setString(2, newDescription);
            pstmt.setString(3, newImagePath);
            pstmt.setString(4, oldName);
            pstmt.executeUpdate();

            System.out.println("Продукт успешно обновлен в базе данных.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при обновлении продукта в базе данных.");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) productListView.getScene().getWindow();
        stage.close(); // Закрытие окна без изменения товара
    }
}