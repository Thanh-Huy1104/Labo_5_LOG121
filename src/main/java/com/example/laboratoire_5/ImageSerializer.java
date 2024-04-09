package com.example.laboratoire_5;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;

public class ImageSerializer {

    // Method to serialize the ImageModel object to a file
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

    // Method to deserialize the ImageModel object from a file
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