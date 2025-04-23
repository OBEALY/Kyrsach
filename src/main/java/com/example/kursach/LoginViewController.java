package com.example.kursach;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoginViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField LoginLabel;

    @FXML
    private TextField PassworsLabel;

    @FXML
    private Button login_Button;

    @FXML
    private Button Button_nonlog;

    @FXML
    void initialize() {
        // Установка обработчика события для кнопки входа
        login_Button.setOnAction(event -> handleLogin());
        Button_nonlog.setOnAction(event -> openHelloView());
    }

    private void handleLogin() {
        String login = LoginLabel.getText();
        String password = PassworsLabel.getText();

        // Проверка логина и пароля
        if ("123".equals(login) && "123".equals(password)) {
            openHelloView();
        } else if ("root".equals(login) && "root".equals(password)) {
            openAdminView();
        } else {
            // Можно добавить сообщение об ошибке
            System.out.println("Неверный логин или пароль");
        }
    }



    private void openHelloView() {
        try {
            // Закрытие текущего окна
            Stage currentStage = (Stage) login_Button.getScene().getWindow();
            currentStage.close();

            // Запуск HelloApplication
            HelloApplication helloApp = new HelloApplication();
            helloApp.start(new Stage()); // Запускаем новый экземпляр
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void openAdminView() {
        try {
            // Закрытие текущего окна
            Stage currentStage = (Stage) login_Button.getScene().getWindow();
            currentStage.close();

            // Запуск HelloApplication
            Admin AdminApp = new Admin();
            AdminApp.start(new Stage()); // Запускаем новый экземпляр
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}