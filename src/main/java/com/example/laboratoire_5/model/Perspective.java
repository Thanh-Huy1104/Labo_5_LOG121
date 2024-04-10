package com.example.laboratoire_5.model;

import com.example.laboratoire_5.controller.Controller;
import javafx.scene.image.ImageView;
import java.io.Serializable;

/**
 * Cette classe représente nos perspectives, donc toutes les nouvelles "images" après avoir subie des modifications de
 * l'utilisateur. Une perspective possède une imageView, car la perspective représente le imageView avec des changements
 * effectués. La classe possède donc en attributs tous les paramètres du imageView qui peuvent être modifiés.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class Perspective implements Serializable {
    // Scale avant une action. Ceci permet l'implémentation du undo pour le zoom
    private double oldScaleX;
    private double oldScaleY;

    // Scale après une action.
    private double scaleX;
    private double scaleY;

    // Valeurs de translations de l'image
    private double translationX;
    private double translationY;

    // Index permettant de différencier toutes les perspectives
    private int index;

    // Référence au imageView qui est modifiée sous de nouvelles perspectives
    private transient ImageView imageView;

    // CONSTRUCTOR
    public Perspective(int index, ImageView imageView) {
        this.index = index;
        this.imageView = imageView;
        this.translationX = imageView.getTranslateX();
        this.translationY = imageView.getTranslateY();
        this.scaleX = imageView.getScaleX();
        this.scaleY = imageView.getScaleY();
    }

    /*
        GETTER AND SETTERS
     */
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

    public double getOldScaleX() {
        return oldScaleX;
    }

    public double getOldScaleY() {
        return oldScaleY;
    }

    /**
     * Cette méthode met à jour les anciens "scale" après qu'un zoom soit effectué
     */
    public void updateScales() {
        oldScaleX = scaleX;
        oldScaleY = scaleY;
    }

    /**
     * Cette méthode permet de modifier les attributs de la perspective après qu'une translation ait été effectuée par
     * l'utilisateur. Lorsqu'une translation est effectuée, le model demande à la perspective affectée de mettre à jour
     * ses attributs aux nouvelles valeurs désirées après la translation.
     *
     * @param data Informations sur le imageView permettant de calculer la translation voulue par l'utilisateur
     * @param deltaX Variation en X
     * @param deltaY Variation en Y
     */
    public void translate(double[] data, double deltaX, double deltaY) {
        double translateX = data[2] + deltaX - data[0];
        double translateY = data[3] + deltaY - data[1];

        // Lorsque l'image est de taille normale, on applique des bordures sur le container de la perspective. Ceci
        // empêche l'utilisateur de faire une translation hors du container.
        if (imageView.getScaleX() == 1 && imageView.getScaleY() == 1) {
            translateX = Math.max(Math.min(translateX, 603 - imageView.getFitWidth()), 0);
            translateY = Math.max(Math.min(translateY, 497 - imageView.getFitHeight()), 0);
        }

        // Mise à jour des paramètres
        setTranslationX(translateX);
        setTranslationY(translateY);
    }

    /**
     * Cette méthode permet de modifier les attributs de la perspective après qu'un zoom ait été effectué par
     * l'utilisateur. Lorsqu'un zoom est effectué, le model demande à la perspective affectée de mettre à jour ses
     * attributs aux nouvelles valeurs désirées après la translation.
     *
     * @param zoomIn paramètre indiquant si le zoom est in ou out
     */
    public void scale(boolean zoomIn) {
        double scaleFactor = zoomIn ? (1.0 / Controller.ZOOM_FACTOR) : Controller.ZOOM_FACTOR;

        // Calcul des nouveaux "scales" dépendamment de zoomIn
        double newScaleX = getScaleX() * scaleFactor;
        double newScaleY = getScaleY() * scaleFactor;

        // Mise à jour des attributs
        setScaleX(newScaleX);
        setScaleY(newScaleY);
    }

    /**
     * Cette méthode est utilisée pour la fonction undo d'un zoom. Elle peut être utilisée pour quoi que ce soit par
     * contre, car elle permet simplement de mettre à jour les attributs de scale de la perspective. Ces attributs
     * peuvent être mis à jour à n'importe quelles valeurs transmises en paramètres.
     *
     * @param scaleX valeur voulue du scale en X
     * @param scaleY valeur voulue du scaele en Y
     */
    public void scaleTo(double scaleX, double scaleY) {
        setScaleX(scaleX);
        setScaleY(scaleY);
        updateScales(); // mise à jours des anciens scales
    }
}