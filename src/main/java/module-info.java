module com.labo5.labo5 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.labo5.labo5 to javafx.fxml;
    exports com.labo5.labo5;
}