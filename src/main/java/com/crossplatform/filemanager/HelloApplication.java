package com.crossplatform.filemanager;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
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
        Menu about = new Menu("О программе");
        Menu exit = new Menu("Выход");
        Menu theme = new Menu("Тема");

        // Создание подпунктов меню
        RadioMenuItem darkTheme = new RadioMenuItem  ("Светлая");
        RadioMenuItem   lightTheme = new RadioMenuItem  ("Темная");
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
        for(City c : City.readFromFile())
        {
            textFromFile += c.toString() + '\n' + '\n';
        }
        textArea.setText(textFromFile);

        //
        //
        //

        Label name = new Label(" Название: ");
        Label population = new Label(" Популяция: ");
        Label area = new Label(" Площадь: ");
        Label yearOfFoundation = new Label(" Год основаня: ");
        Label countOfSchool = new Label(" Кол-во школ: ");

        TextField nameText = new TextField();
        TextField populationText = new TextField();
        TextField areaText = new TextField();
        TextField yearOfFoundationText = new TextField();
        TextField countOfSchoolText = new TextField();

        Button add = new Button("Добавить");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(nameText.getText() == "")
                {
                    showAlert("Поле Название обязательно для ввода!");
                    return;
                }

                try
                {
                    int population = Integer.parseInt(populationText.getText());
                    double area = Double.parseDouble(areaText.getText());
                    int yearOfFoundation = Integer.parseInt(yearOfFoundationText.getText());
                    int countOfSchool = Integer.parseInt(countOfSchoolText.getText());

                    City city = new City(nameText.getText(), population, area, yearOfFoundation, countOfSchool);
                    city.writeToFile(true);
                }
                catch (Exception ex)
                {
                    showAlert("Ошибка ввода не числового значения. Все поля обязательны для ввода!");
                    ex.printStackTrace();
                    return;
                }

                String textFromFile = "";
                for(City c : City.readFromFile())
                {
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

        FlowPane formInner = new FlowPane(10, 10, name, nameText, population, populationText, area, areaText);
        FlowPane formInner2 = new FlowPane(10, 10, yearOfFoundation, yearOfFoundationText, countOfSchool, countOfSchoolText);
        FlowPane formInner3 = new FlowPane(10, 10, add);
        VBox form = new VBox(10, formInner, formInner2, formInner3);

        // search, update, delete here ...
        Label idCountry = new Label("Название города: ");
        TextField idCountryText = new TextField();
        Button delete = new Button("Удалить");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = idCountryText.getText();
                if(text == "") {
                    showAlert("Введите пожалуйста название");
                    return;
                }

                ArrayList<City> cities = City.readFromFile();
                String test = "";
                for(City c : cities)
                {
                    test += c.getName() + " " + text + "\n";
                    if(c.getName().equals(text))
                    {
                        City.deleteFromFile(c);
                        showAlert("Удаление города " + text + " прошло успешно");
                        break;
                    }
                }
                showAlert(test);

                String textFromFile = "";
                for(City c : City.readFromFile())
                {
                    textFromFile += c.toString() + '\n' + '\n';
                }
                textArea.setText(textFromFile);

                idCountryText.clear();
            }
        });

        VBox deleting = new VBox(10, idCountry, idCountryText, delete);

        Label categoryLabel = new Label("Выберите категорию поиска: ");
        ObservableList<String> langs = FXCollections.observableArrayList("Название", "Популяция", "Площадь", "Год основания", "Кол-во школ");
        ComboBox<String> langsComboBox = new ComboBox<String>(langs);
        langsComboBox.setValue("Название"); // устанавливаем выбранный элемент по умолчанию
        VBox categoryChoise = new VBox(10, categoryLabel, langsComboBox);

        Label searchLabel = new Label("Введите поисковой запрос: ");
        TextField searchText = new TextField();
        Button search = new Button("Найти");
        VBox wordSearch = new VBox(10, searchLabel, searchText, search);

        VBox searching = new VBox(10, categoryChoise, wordSearch);
        VBox controls = new VBox(100, deleting, searching);
        FlowPane content = new FlowPane(10, 10, textArea, controls);

        VBox root = new VBox(10, top, form, content);
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setTitle("Редактор городов");
        stage.setWidth(1000);
        stage.setHeight(650);

        stage.show();
    }

    private void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}