package com.example.laboratoire_5;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageModel implements Subject, Serializable {
    private transient Image originalImage;
    private String imagePath;
    private List<Perspective> perspectiveList;
    private Perspective perspective1;
    private Perspective perspective2;
    private transient List<Observer> observers;

    public ImageModel(Perspective perspective1, Perspective perspective2) {
        this.perspective1 = perspective1;
        this.perspective2 = perspective2;
        this.perspectiveList = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.imagePath = null;
    }

    public ImageModel() {
        this.originalImage = null; // Vous devez obtenir l'image d'une autre manière, car elle n'est pas fournie ici
        this.perspectiveList = new ArrayList<>(); // Initialisez la liste
        perspectiveList.add(perspective1);
        perspectiveList.add(perspective2);
        this.observers = new ArrayList<>(); // Assurez-vous d'initialiser cette liste également
    }

    public void addPerspective(Perspective perspective) {
        perspectiveList.add(perspective);
    }

    public Perspective getCurrentPerspective(int index) {
        for (Perspective perspective : perspectiveList) {
            if (perspective.getIndex() == index) {
                return perspective;
            }
        }

        return null;
    }

    public void setCurrentPerspective(int index, Perspective newP) {
        for (Perspective perspective : perspectiveList) {
            if (perspective.getIndex() == index) {
                perspective = newP;
            }
        }
        notifyObservers();
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void modifyScalePerspective(int index, boolean zoomIn) {
        Perspective perspective = getCurrentPerspective(index);
        if (perspective != null) {

            perspective.scale(zoomIn);
            notifyObservers();
        }
    }

    public void modifyTranslationPerspective(int index, double[] data, double deltaX, double deltaY) {
        Perspective perspective = getCurrentPerspective(index);
        perspective.translate(data, deltaX, deltaY);
        notifyObservers();
    }

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

    public void restoreFromMemento(Memento m, int index) {
        Perspective perspective = getCurrentPerspective(index);

        if (m.getAction().equals("Translation")) {
            double[] imageViewData = m.getImageViewData(index);
            if (imageViewData != null) {
                // Vous devez vérifier si imageViewData a une longueur suffisante avant d'accéder à ses éléments
                if (imageViewData.length >= 4) {
                    // Main thing here is that we need to update the views with the new data from the controller
                    perspective.translate(imageViewData, imageViewData[0], imageViewData[1]);
                    notifyObservers();
                }
            }
        } else if (m.getAction().equals("Zoom")) {
            double[] scales = m.getScales(index);
            perspective.scaleTo(scales[0], scales[1]);
            notifyObservers();
        }
    }

    public void setObservers() {
        this.observers = new ArrayList<>();
    }

    public void updateModel(ImageView im1, ImageView im2) {
        // Since ImageViews are not serialized, we need to reassign them
        for (Perspective perspective : perspectiveList) {
            if (perspective.getIndex() == 1) {
                perspective.setImageView(im1);
                perspective.updateScales();
            } else if (perspective.getIndex() == 2) {
                perspective.setImageView(im2);
                perspective.updateScales();
            }
        }

        notifyObservers();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        this.originalImage = new Image(imagePath); // Reload the image from the path
    }

    public void setOriginalImage(Image originalImage, String imagePath) {
        this.originalImage = originalImage;
        this.imagePath = imagePath;
    }
}