package com.example.kursach;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ProductViewController {

    @FXML
    private ImageView productImage;

    @FXML
    private Text productName;

    @FXML
    private Text productDescription;

    private Product currentProduct; // Хранит текущий продукт

    public void setProduct(Product product) {
        // Убедитесь, что продукт не равен null
        if (product != null) {
            currentProduct = product; // Сохраняем текущий продукт
            Image image = null;
            String imagePath = product.getImagePath(); // Получаем полный путь к изображению
            System.out.println("Загрузка изображения из пути: " + imagePath);

            try {
                File file = new File(imagePath);
                if (!file.exists()) {
                    System.err.println("Файл не найден: " + imagePath);
                    // Установите изображение по умолчанию здесь
                    image = new Image("path_to_default_image.jpg");
                } else {
                    image = new Image(new FileInputStream(file)); // Загружаем изображение по полному пути
                }
            } catch (FileNotFoundException e) {
                System.err.println("Ошибка: изображение не найдено: " + imagePath);
                image = new Image("path_to_default_image.jpg"); // Укажите путь к изображению по умолчанию
            } catch (Exception e) {
                System.err.println("Неизвестная ошибка: " + e.getMessage());
            }

            // Установка изображения и текста
            productImage.setImage(image);
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());

            // Автоматическое добавление продукта в избранное
            addToFavorites(currentProduct);
        } else {
            System.err.println("Product is null");
        }
    }

    private void addToFavorites(Product product) {
        // Логика добавления товара в избранное
        // Например, вы можете сохранить его в каком-то списке или базе данных
        showAlert("Товар добавлен в избранное", product.getName() + " был успешно добавлен в избранное.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}