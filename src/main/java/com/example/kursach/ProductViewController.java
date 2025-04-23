package com.example.kursach;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.control.Button;

public class ProductViewController {

    @FXML
    private ImageView productImage;

    @FXML
    private Text productName;

    @FXML
    private Text productDescription;

    @FXML
    private Button Button_enter;

    public void setProduct(Product product) {
        // Убедитесь, что продукт не равен null
        if (product != null) {
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
        } else {
            System.err.println("Product is null");
        }
    }
}