module com.example.amc26 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;
    requires kotlin.stdlib;


    opens com.example.amc26 to javafx.fxml, com.google.gson;
    exports com.example.amc26;
}