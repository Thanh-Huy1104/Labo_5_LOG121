module com.example.laboratoire_5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jfxrt;


    opens com.example.laboratoire_5 to javafx.fxml;
    exports com.example.laboratoire_5;
}