package com.example.laboratoire_5.model;

import com.example.laboratoire_5.controller.Memento;
import com.example.laboratoire_5.observer.Observer;
import com.example.laboratoire_5.observer.Subject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet d'avoir un modèle MVC logique. Cette classe représente notre model, donc toute l'information
 * nécessaire au bon fonctionnement de l'application. Elle est le sujet de notre patron observeur, donc elle implémente
 * toutes les méthodes de l'interface Subject. Elle implémente également Serializable, car c'est le model que nous
 * sauvegardons dans un fichier. Le data principal conservé par le model est sa liste de perspective. Ce sont celles-ci
 * qui sont modifiées par les actions de l'utilisateur.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class ImageModel implements Subject, Serializable {
    private transient Image originalImage;
    private String imagePath;
    private List<Perspective> perspectiveList;
    private transient List<Observer> observers;

    // CONSTRUCTOR
    public ImageModel() {
        this.perspectiveList = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.imagePath = null;
    }

    // Méthode pour ajouter une perspective
    public void addPerspective(Perspective perspective) {
        perspectiveList.add(perspective);
    }

    /**
     * Méthode permettant d'obtenir la perspective actuelle selon un index
     *
     * @param index index de la perspective dont on veut la perspective actuelle
     * @return La perspective actuelle associée à l'index
     */
    public Perspective getCurrentPerspective(int index) {
        for (Perspective perspective : perspectiveList) {
            if (perspective.getIndex() == index) {
                return perspective;
            }
        }

        return null;
    }

    /**
     * Méhode permettant de "set" une nouvelle perspective actuelle selon un index
     *
     * @param index index de la perspective que l'on veut remplacer
     * @param newP nouvelle perspective
     */
    public void setCurrentPerspective(int index, Perspective newP) {
        for (Perspective perspective : perspectiveList) {
            if (perspective.getIndex() == index) {
                perspective = newP;
            }
        }
        notifyObservers();
    }

    /**
     * Méthode du patron observeur permettant d'aviser tous les observateurs lorsqu'un changement est effectué
     */
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    // Ajout d'un observateur
    public void attach(Observer observer) {
        observers.add(observer);
    }

    // Retrait d'un observateur
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Méthode du patron commande permettant de faire le ZOOM sur une perspective
     *
     * @param index index de la perspective sur laquelle on veut faire le ZOOM
     * @param zoomIn valeur booléenne indiquant si c'est une zoom in ou out
     */
    public void modifyScalePerspective(int index, boolean zoomIn) {
        Perspective perspective = getCurrentPerspective(index);
        if (perspective != null) {

            perspective.scale(zoomIn);
            notifyObservers(); // On avise les observateurs
        }
    }

    /**
     * Méthode du patron commande permettant de faire la TRANSLATION sur une perspective
     *
     * @param index index de la perspective sur laquelle on veut faire la TRANSLATION
     * @param data data nécessaire pour la translation
     * @param deltaX variation en X
     * @param deltaY variation en Y
     */
    public void modifyTranslationPerspective(int index, double[] data, double deltaX, double deltaY) {
        Perspective perspective = getCurrentPerspective(index);
        perspective.translate(data, deltaX, deltaY);
        notifyObservers(); // On avise les observateurs du changement
    }

    /**
     * Méthode du patron memento permettant de créer un memento avec l'état actuel du model
     *
     * @param index l'index permet de différencier les deux perspectives
     * @param action Nom de l'action pour pouvoir différencier une translation d'un zoom
     * @return le memento avec l'état actuel du model
     */
    public Memento saveToMemento(int index, String action) {
        
        Memento memento = new Memento(action);
        Perspective perspective = getCurrentPerspective(index);
        
        if (action.equals("Translation")) {
            double[] imageViewData = (double[]) perspective.getImageView().getUserData();
            memento.setImageViewData(index, imageViewData);
        } else if (action.equals("Zoom")) {
            double[] scales = new double[]{perspective.getOldScaleX(), perspective.getOldScaleY()};
            memento.setScales(index, scales);
        }

        return memento;
    }

    /**
     * Méthode du patron memento permettant de faire un undo. Cette méthode prend en paramètre le memento contenant
     * l'ancien état du model. L'état contenu dans ce memento devient l'état actuel du model.
     *
     * @param m Memento dont on veut récupérer l'état
     * @param index l'index permet de différencier les deux perspectives
     */
    public void restoreFromMemento(Memento m, int index) {
        Perspective perspective = getCurrentPerspective(index);

        if (m.getAction().equals("Translation")) {
            double[] imageViewData = m.getImageViewData(index);
            if (imageViewData != null) {
                if (imageViewData.length >= 4) {
                    // On fait l'action à partir des données de l'ancien memento, ce qui remet en place l'image
                    perspective.translate(imageViewData, imageViewData[0], imageViewData[1]);
                    notifyObservers(); // on avise les observateurs du changement
                }
            }
        } else if (m.getAction().equals("Zoom")) {
            double[] scales = m.getScales(index);
            // On fait l'action à partir des données de l'ancien memento, ce qui remet en place l'image
            perspective.scaleTo(scales[0], scales[1]);
            notifyObservers(); // on avise les observateurs du changement
        }
    }

    // Instanciation de la liste des observateurs
    public void setObservers() {
        this.observers = new ArrayList<>();
    }

    /**
     * Méthode permettant la restoration du model lorsque l'on charge un modèle qui avait été serializé. Ceci gère
     * principalement l'initialisation de quelques éléments que le model ne possède pas vu qu'il vient d'être chargé
     *
     * @param im1 imageView de la première perspective
     * @param im2 imageView de la deuxième perspective
     */
    public void updateModel(ImageView im1, ImageView im2) {
        // On associe les imageViews aux perspectives actuelles du nouveau model
        for (Perspective perspective : perspectiveList) {
            if (perspective.getIndex() == 1) {
                perspective.setImageView(im1);
                perspective.updateScales();
            } else if (perspective.getIndex() == 2) {
                perspective.setImageView(im2);
                perspective.updateScales();
            }
        }

        notifyObservers(); // on avise les observateurs de ce changement
    }

    // GETTER
    public String getImagePath() {
        return imagePath;
    }

    // SETTER
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        this.originalImage = new Image(imagePath); // On obtient l'image à partir du path
    }

    // SETTER
    public void setOriginalImage(Image originalImage, String imagePath) {
        this.originalImage = originalImage;
        this.imagePath = imagePath;
    }
}