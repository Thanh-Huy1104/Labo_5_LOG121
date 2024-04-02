package com.example.laboratoire_5;

import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;

public class Perspective {
    private double scale;
    private double translationX;
    private double translationY;
    private int index;

    private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public double getTranslationX() {
        return translationX;
    }

    public void setTranslationX(double translationX) {
        this.translationX = translationX;
    }

    public double getTranslationY() {
        return translationY;
    }

    public void setTranslationY(double translationY) {
        this.translationY = translationY;
    }

    public int getIndex() {
        return index;
    }

    public void translate() {
        translationX += deltaX;
        translationY += deltaY;
        applyTransformations();
    }
}
