package com.example.laboratoire_5;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Classe principale de l'application contenant les méthodes start et main. C'est avec cette classe qu'on peut
 * démarrer toute l'application JavaFX.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class Application extends javafx.application.Application {
    // Méthode de JavaFX permettant de démarrer l'application
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Image editor");
        stage.setScene(scene);
        stage.show();
    }

    // Méthode main permettant de démarrer l'application
    public static void main(String[] args) {
        launch();
    }
}