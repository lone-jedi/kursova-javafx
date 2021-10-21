module com.crossplatform.filemanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.crossplatform.filemanager to javafx.fxml;
    exports com.crossplatform.filemanager;
}