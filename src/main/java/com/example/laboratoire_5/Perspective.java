package com.example.laboratoire_5;

import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;

public class Perspective {
    private double scale;
    private double translationX;
    private double translationY;
    private int index;
    private ImageView imageView;

    public Perspective(int index, ImageView imageView) {
        this.index = index;
        this.imageView = imageView;
        this.translationX = imageView.getTranslateX();
        this.translationY = imageView.getTranslateY();
    }

    public Perspective(Perspective perspective) {
        this.index = perspective.getIndex();
        this.imageView = perspective.getImageView();
    }

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

    public Perspective clone() {
        Perspective perspective = new Perspective(this);

        return  perspective;
    }

    public void translate(double[] data, double deltaX, double deltaY) {
        double translateX = data[2] + deltaX - data[0];
        double translateY = data[3] + deltaY - data[1];
        imageView.setTranslateX(translateX);
        imageView.setTranslateY(translateY);
    }
}
