package com.crossplatform.filemanager;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Создание меню
        MenuBar menuBar = new MenuBar();

        // Создание пунктов меню
        Menu about = new Menu("Справка");
        Menu exit = new Menu("Вихід");
        Menu theme = new Menu("Тема");

        // Создание подпунктов меню
        RadioMenuItem darkTheme = new RadioMenuItem  ("Світла");
        RadioMenuItem   lightTheme = new RadioMenuItem  ("Темна");
        ToggleGroup group = new ToggleGroup(); // Свяжем подпункты в radiomenu
        lightTheme.setToggleGroup(group);
        darkTheme.setToggleGroup(group);
        lightTheme.setSelected(true);

        // Добавим элементы подпунктов в меню
        theme.getItems().addAll(lightTheme, darkTheme);

        menuBar.getMenus().addAll(theme, about, exit);

        BorderPane top = new BorderPane();
        top.setTop(menuBar);

        TextArea textArea = new TextArea();
        textArea.setPrefColumnCount(50);
        textArea.setPrefRowCount(25);

        String textFromFile = "";
        for(City c : City.readFromFile()) {
            textFromFile += c.toString() + '\n' + '\n';
        }
        textArea.setText(textFromFile);

        //
        //
        //

        Label name = new Label(" Назва: ");
        Label population = new Label(" Популяція: ");
        Label area = new Label(" Площина: ");
        Label yearOfFoundation = new Label(" Рік засновнення: ");
        Label countOfSchool = new Label(" Кількість шкіл: ");

        TextField nameText = new TextField();
        TextField populationText = new TextField();
        TextField areaText = new TextField();
        TextField yearOfFoundationText = new TextField();
        TextField countOfSchoolText = new TextField();

        Button add = new Button("Додати");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(nameText.getText() == "") {
                    showAlert("Поле Назва обов'язкове для вводу!");
                    return;
                }

                try {
                    int population = Integer.parseInt(populationText.getText());
                    double area = Double.parseDouble(areaText.getText());
                    int yearOfFoundation = Integer.parseInt(yearOfFoundationText.getText());
                    int countOfSchool = Integer.parseInt(countOfSchoolText.getText());

                    City city = new City(nameText.getText(), population, area, yearOfFoundation, countOfSchool);
                    city.writeToFile(true);
                } catch (Exception ex) {
                    showAlert("Помилка! Введено не число. Усі поля обов'язкові для вводу");
                    ex.printStackTrace();
                    return;
                }

                String textFromFile = "";
                for(City c : City.readFromFile()) {
                    textFromFile += c.toString() + '\n' + '\n';
                }
                textArea.setText(textFromFile);

                nameText.clear();
                populationText.clear();
                areaText.clear();
                yearOfFoundationText.clear();
                countOfSchoolText.clear();
            }
        });

        VBox form = new VBox(10,
                new FlowPane(10, 10, name, nameText, population, populationText, area, areaText),
                new FlowPane(10, 10, yearOfFoundation, yearOfFoundationText, countOfSchool, countOfSchoolText),
                new FlowPane(10, 10, add));

        // search, update, delete here ...
        Label idCountry = new Label("Назва міста: ");
        TextField idCountryText = new TextField();

        Button delete = new Button("Видалити");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = idCountryText.getText();
                if(text == "") {
                    showAlert("Введіть будь-ласка назву");
                    return;
                }

                ArrayList<City> cities = City.readFromFile();
                String test = "";
                for(City c : cities) {
                    test += c.getName() + " " + text + "\n";
                    if(c.getName().equals(text)) {
                        City.deleteFromFile(c);
                        showAlert("Видалення міста " + text + " пройшло успішно");
                        break;
                    }
                }
                showAlert(test);

                String textFromFile = "";
                for(City c : City.readFromFile()) {
                    textFromFile += c.toString() + '\n' + '\n';
                }
                textArea.setText(textFromFile);

                idCountryText.clear();
            }
        });

        VBox deleting = new VBox(10, idCountry, idCountryText, delete);

        Label categoryLabel = new Label("Оберіть категорію пошуку: ");
        ObservableList<String> cityOptions = FXCollections.observableArrayList(
                "Назва", "Популяція", "Площина", "Рік засновнення", "Кількість шкіл");
        ComboBox<String> cityOptionsComboBox = new ComboBox<String>(cityOptions);
        cityOptionsComboBox.setValue("Название"); // устанавливаем выбранный элемент по умолчанию
        VBox categoryChoice = new VBox(10, categoryLabel, cityOptionsComboBox);

        Label searchLabel = new Label("Введіть пошуковий запит: ");
        TextField searchText = new TextField();
        Button search = new Button("Знайти");
        VBox wordSearch = new VBox(10, searchLabel, searchText, search);

        VBox searching = new VBox(10, categoryChoice, wordSearch);
        VBox controls = new VBox(100, deleting, searching);
        FlowPane content = new FlowPane(10, 10, textArea, controls);

        VBox root = new VBox(10, top, form, content);
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setTitle("Редактор міст");
        stage.setWidth(1000);
        stage.setHeight(650);

        stage.show();
    }

    private void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Помилка");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}