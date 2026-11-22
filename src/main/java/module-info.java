module com.example.amc26 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;


    opens com.example.amc26 to javafx.fxml;
    exports com.example.amc26;
}