module com.example.amc26 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.amc26 to javafx.fxml;
    exports com.example.amc26;
}