package com.example.laboratoire_5;

import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;

public class Perspective {
    private double scaleX;
    private double scaleY;
    private double translationX;
    private double translationY;
    private int index;
    private ImageView imageView;


    public Perspective(int index, ImageView imageView) {
        this.index = index;
        this.imageView = imageView;
        this.translationX = imageView.getTranslateX();
        this.translationY = imageView.getTranslateY();
        this.scaleX = imageView.getScaleX();
        this.scaleY = imageView.getScaleY();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }
    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
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

    public void translate(double[] data, double deltaX, double deltaY) {
        System.out.println("Data :" + data[2] + " " + data[0] + "  deltaX : " +
                deltaX + "   deltaY : " + deltaY);
        double translateX = data[2] + deltaX - data[0];
        double translateY = data[3] + deltaY - data[1];

        translateX = Math.max(Math.min(translateX, 603 - imageView.getFitWidth()), 0);
        translateY = Math.max(Math.min(translateY, 497 - imageView.getFitHeight()), 0);

        setTranslationX(translateX);
        setTranslationY(translateY);

        System.out.println("Translate X : " + this.translationX);
        System.out.println("Translate Y : " + this.translationY);
    }
}
