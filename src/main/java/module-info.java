module com.example.laboratoire_5 {
    requires javafx.controls;
    requires javafx.fxml;



    opens com.example.laboratoire_5 to javafx.fxml;
    exports com.example.laboratoire_5;
    exports com.example.laboratoire_5.views;
    opens com.example.laboratoire_5.views to javafx.fxml;
    exports com.example.laboratoire_5.model;
    opens com.example.laboratoire_5.model to javafx.fxml;
    exports com.example.laboratoire_5.controller;
    opens com.example.laboratoire_5.controller to javafx.fxml;
    exports com.example.laboratoire_5.observer;
    opens com.example.laboratoire_5.observer to javafx.fxml;
}