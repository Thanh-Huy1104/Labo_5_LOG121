package com.example.laboratoire_5;
import java.io.*;

public class ImageSerializer {

    public boolean serialize(ImageModel imageModel, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(imageModel.getOriginalImage().getData());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ImageModel deserialize(String filename) {
        ImageModel imageModel = new ImageModel();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            byte[] data = (byte[]) in.readObject();
            Image image = new Image();
            image.setData(data);
            imageModel.setOriginalImage(image);
            return imageModel;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
