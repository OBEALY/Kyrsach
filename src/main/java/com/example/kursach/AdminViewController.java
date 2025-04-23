package com.example.kursach;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AdminViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField SearchText;

    @FXML
    private Button Button_ViewPC;

    @FXML
    private Button view_Button;

    @FXML
    private TilePane TilePanel;

    @FXML
    private ScrollPane ScrollPanel1;

    @FXML
    private Button up_Button1;

    @FXML
    private Button Back_Button;

    @FXML
    private Button end_Button; // Кнопка завершения

    @FXML
    private ComboBox<?> SortBox;




    @FXML
    private void onSortAction() {
        String selectedItem = (String) SortBox.getValue();
        List<Product> allProducts = ProductManager.getInstance().getProducts();

        if (selectedItem != null) {
            switch (selectedItem) {
                case "Сортировать по алфавиту":
                    allProducts.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
                    break;
                case "Сортировать по обратному алфавиту":
                    allProducts.sort((p1, p2) -> p2.getName().compareTo(p1.getName()));
                    break;
            }
            currentPage = 0; // Сброс текущей страницы после сортировки
            loadProducts(); // Обновляем продукты
        }
    }


    private int itemsPerPage = 4;
    private int currentPage = 0;

    @FXML
    void initialize() {
        assert Button_ViewPC != null : "fx:id=\"Button_ViewPC\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert view_Button != null : "fx:id=\"view_Button\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert TilePanel != null : "fx:id=\"TilePanel\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert ScrollPanel1 != null : "fx:id=\"ScrollPanel1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert up_Button1 != null : "fx:id=\"up_Button1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Back_Button != null : "fx:id=\"Back_Button\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert end_Button != null : "fx:id=\"end_Button\" was not injected: check your FXML file 'hello-view.fxml'.";

        ScrollPanel1.setFitToWidth(true);
        ScrollPanel1.setFitToHeight(true);
        TilePanel.setVisible(false);
        ScrollPanel1.setVisible(false);
        up_Button1.setVisible(false);
        Back_Button.setVisible(false);

        // Устанавливаем обработчики кнопок
        Button_ViewPC.setOnAction(actionEvent -> showProducts());
        view_Button.setOnAction(actionEvent -> showCompanyInfo());
        up_Button1.setOnAction(actionEvent -> nextPage());
        Back_Button.setOnAction(actionEvent -> previousPage());
        end_Button.setOnAction(actionEvent -> closeApplication());
        SearchText.setOnAction(actionEvent -> handleSearch());
    }

    public void setProducts(List<Product> products) {
        ProductManager.getInstance().setProducts(products);
    }

    private void showProducts() {
        TilePanel.setVisible(true);
        ScrollPanel1.setVisible(true);
        up_Button1.setVisible(true);
        Back_Button.setVisible(true);
        currentPage = 0; // Сброс текущей страницы
        loadProducts(); // Загрузка продуктов при нажатии
    }

    void loadProducts() {
        TilePanel.getChildren().clear();
        List<Product> allProducts = ProductManager.getInstance().getProducts();

        if (allProducts.isEmpty()) {
            Text noProductsMessage = new Text("Нет доступных продуктов.");
            TilePanel.getChildren().add(noProductsMessage);
            return; // Завершение метода, если нет продуктов
        }

        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, allProducts.size());

        for (int i = startIndex; i < endIndex; i++) {
            VBox productBox = createProductBox(allProducts.get(i));
            TilePanel.getChildren().add(productBox);
        }

        // Обновление состояния кнопок "Вперед" и "Назад"
        up_Button1.setDisable((currentPage + 1) * itemsPerPage >= allProducts.size());
        Back_Button.setDisable(currentPage == 0);
    }

    private VBox createProductBox(Product product) {
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

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(120);
        imageView.setFitWidth(190);
        imageView.setPreserveRatio(true);

        Text name = new Text(product.getName());
        Text description = new Text(product.getDescription());

        VBox productBox = new VBox(imageView, name, description);
        productBox.setSpacing(5);
        productBox.setPrefWidth(190);
        productBox.setMinSize(190, Region.USE_PREF_SIZE);
        productBox.setMaxWidth(190);

        productBox.setOnMouseClicked(event -> showProductDetails(product));

        return productBox;
    }

    private void showProductDetails(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("product-view.fxml"));
            VBox productView = loader.load();

            ProductViewController controller = loader.getController();
            if (controller != null) {
                controller.setProduct(product);
            }

            Stage productStage = new Stage();
            productStage.setTitle(product.getName());
            productStage.setScene(new Scene(productView));
            productStage.setResizable(false);
            productStage.initStyle(StageStyle.UTILITY);

            productStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void nextPage() {
        if ((currentPage + 1) * itemsPerPage < ProductManager.getInstance().getProducts().size()) {
            currentPage++;
            loadProducts();
        }
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadProducts();
        }
    }

    private void showCompanyInfo() {
        TilePanel.getChildren().clear();
        up_Button1.setVisible(false);
        Back_Button.setVisible(false);

        Text companyInfo = new Text("Добро пожаловать в нашу компанию! Мы предоставляем высококачественные товары и услуги, "
                + "ориентированные на потребности клиентов. Наша цель - обеспечить лучший сервис и удовлетворение потребностей "
                + "каждого клиента. Спасибо, что выбрали нас!\n\n"
                + "Наш магазин работает с 2005 года и предлагает огромный выбор компьютерной техники и периферии. "
                + "Мы стараемся обеспечить наших клиентов только лучшими продуктами от проверенных производителей. "
                + "Наша команда профессионалов готова помочь вам в выборе необходимого оборудования и предоставить "
                + "высококачественное обслуживание в любое время.\n\n");

        companyInfo.setWrappingWidth(400);
        companyInfo.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-fill: black;");

        TilePanel.getChildren().add(companyInfo);
        TilePanel.setVisible(true);
        ScrollPanel1.setVisible(true);
    }

    private void closeApplication() {
        Platform.exit(); // Завершение приложения
    }


    @FXML

    private void handleSearch() {
        String query = SearchText.getText().trim(); // Получаем текст из поля и убираем лишние пробелы
        List<Product> allProducts = ProductManager.getInstance().getProducts();

        TilePanel.getChildren().clear(); // Очищаем текущие продукты

        if (query.isEmpty()) {
            // Если поле поиска пустое, просто загружаем все продукты
            loadProducts();
            return;
        }

        // Фильтруем продукты по названию с помощью обычного цикла
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }

        if (filteredProducts.isEmpty()) {
            Text noResultsMessage = new Text("Продукты не найдены.");
            TilePanel.getChildren().add(noResultsMessage);
        } else {
            for (Product product : filteredProducts) {
                VBox productBox = createProductBox(product);
                TilePanel.getChildren().add(productBox);
            }
        }

        // Обновление состояния кнопок "Вперед" и "Назад"
        up_Button1.setDisable(true); // Скрываем кнопку "Вперед" при поиске
        Back_Button.setDisable(true); // Скрываем кнопку "Назад" при поиске
    }
}