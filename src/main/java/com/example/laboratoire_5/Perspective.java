package com.example.laboratoire_5;

import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;

import java.io.Serializable;

public class Perspective implements Serializable {
    private double oldScaleX;
    private double oldScaleY;
    private double scaleX;
    private double scaleY;
    private double translationX;
    private double translationY;
    private int index;
    private boolean zoomIn;
    private transient ImageView imageView;

    public Perspective(int index, ImageView imageView) {
        this.index = index;
        this.imageView = imageView;
        this.translationX = imageView.getTranslateX();
        this.translationY = imageView.getTranslateY();
        this.scaleX = imageView.getScaleX();
        System.out.println("scale X : " + scaleX);
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

    public boolean isZoomIn() {
        return zoomIn;
    }

    public void setZoomIn(boolean zoomIn) {
        this.zoomIn = zoomIn;
    }

    public double getOldScaleX() {
        return oldScaleX;
    }

    public double getOldScaleY() {
        return oldScaleY;
    }

    public void setOldScaleX(double oldScaleX) {
        this.oldScaleX = oldScaleX;
    }

    public void setOldScaleY(double oldScaleY) {
        this.oldScaleY = oldScaleY;
    }

    public void updateScales() {
        oldScaleX = scaleX;
        oldScaleY = scaleY;
    }

    public void translate(double[] data, double deltaX, double deltaY) {
        double translateX = data[2] + deltaX - data[0];
        double translateY = data[3] + deltaY - data[1];

        //ces deux lignes de codes sont pour la limite de translation si on en a besoin.
        if (imageView.getScaleX() == 1 && imageView.getScaleY() == 1) {
            translateX = Math.max(Math.min(translateX, 603 - imageView.getFitWidth()), 0);
            translateY = Math.max(Math.min(translateY, 497 - imageView.getFitHeight()), 0);
        }

        setTranslationX(translateX);
        setTranslationY(translateY);
    }

    public void scale(boolean zoomIn) {
        double scaleFactor = zoomIn ? (1.0 / Controller.ZOOM_FACTOR) : Controller.ZOOM_FACTOR;
        System.out.println("scale X : " + scaleX);

        // Calculate new scale factors
        double newScaleX = getScaleX() * scaleFactor;
        double newScaleY = getScaleY() * scaleFactor;

        System.out.println("New scale X : " + newScaleX);

        // Apply the new scale factors and translations
        setScaleX(newScaleX);
        setScaleY(newScaleY);

    }



    public void scaleTo(double scaleX, double scaleY) {
        System.out.println("Current scales : " + this.scaleX + " " + this.scaleY + "\tScales retrieved : " + scaleX + scaleY);
        setScaleX(scaleX);
        setScaleY(scaleY);
        updateScales();
    }
}