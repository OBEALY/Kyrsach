package com.example.kursach;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.ComboBox;
import java.io.IOException;

public class Login extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 248, 406);

        // Установка заголовка окна
        // Запрет изменения размера окна
        stage.setResizable(false);

        // Установка стиля окна для отображения только кнопок "Свернуть" и "Закрыть"
        stage.initStyle(StageStyle.UTILITY);

        // Установка сцены и отображение окна
        stage.setScene(scene);
        stage.show();

        // Автоматический запуск сервера и клиента
        startProductServer();
    }
    private void startProductServer() {
        // Создание и запуск сервера в новом потоке
        new Thread(() -> {
            try {
                ProductServer server = new ProductServer(); // Предполагаем, что у вас есть конструктор по умолчанию
                server.start(); // Запуск сервера
            } catch (Exception e) {
                System.err.println("Ошибка при запуске сервера: " + e.getMessage());
            }
        }).start();
    }


    public static void main(String[] args) {
        launch();
    }
}