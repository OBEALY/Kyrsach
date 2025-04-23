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

public class DeleteProductController {

    @FXML
    private ListView<String> productListView; // Список для выбора продукта

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
    private void handleDeleteProduct() {
        String selectedProduct = productListView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            deleteProductFromDatabase(selectedProduct); // Удаляем продукт из БД

            // Закрытие окна
            Stage stage = (Stage) productListView.getScene().getWindow();
            stage.close(); // Закрываем форму
        } else {
            System.out.println("Пожалуйста, выберите продукт для удаления.");
        }
    }

    private void deleteProductFromDatabase(String productName) {
        String url = "jdbc:mysql://localhost:3306/ServerJava"; // URL вашей БД
        String user = "root"; // Укажите имя пользователя
        String password = "root"; // Укажите пароль

        String query = "DELETE FROM products WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productName);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Продукт успешно удален из базы данных.");
            } else {
                System.out.println("Продукт с указанным именем не найден.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при удалении продукта из базы данных.");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) productListView.getScene().getWindow();
        stage.close(); // Закрытие окна без удаления товара
    }
}