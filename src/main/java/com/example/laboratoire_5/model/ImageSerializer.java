package com.example.laboratoire_5.model;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;

/**
 * Cette classe permet de sauvegarder une image et ses perspectives actuelles grâce à la serialization de java.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class ImageSerializer {

    /**
     * Cette méthode fait la serialization de notre model dans un fichier sélectionné par l'utilisateur
     *
     * @param model model que l'on veut sauvegarder
     */
    public static void serializeImageModel(ImageModel model) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image Model");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Serialized Files", "*.ser"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(model);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cette méthode permet de récupérer un model qui a été sauvegardé avant (image et perspectives actuelles).
     *
     * @return le nouveau model récupéré
     */
    public static ImageModel deserializeImageModel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image Model");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Serialized Files", "*.ser"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            ImageModel model = null;
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                model = (ImageModel) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return model;
        }
        return null;
    }
}