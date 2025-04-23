package com.example.kursach;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Admin extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin-view.fxml"));



        ProductClient client = new ProductClient();

        // Создание списка продуктов, используя метод из ProductClient
        List<Product> products = client.fetchProducts("192.168.198.1", 12345); // Замените на нужный адрес и порт


        // Загрузка FXML в сцену
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);

        // Установка заголовка окна
        stage.setTitle("TechoDom");
        stage.setResizable(false);
        stage.setScene(scene);

        // Получение контроллера и передача продуктов
        AdminViewController controller = fxmlLoader.getController();
        controller.setProducts(products); // Передача продуктов после загрузки сцены

        // Отображение окна
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}