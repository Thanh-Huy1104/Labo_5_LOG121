package com.example.laboratoire_5;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImageModel implements Subject {

    private Image originalImage;
    private List<Perspective> perspectiveList;
    private Perspective perspective1;
    private Perspective perspective2;
    private List<Observer> observers;

    public ImageModel(Perspective perspective1, Perspective perspective2) {
        this.perspective1 = perspective1;
        this.perspective2 = perspective2;
        this.perspectiveList = new ArrayList<>();
        this.observers = new ArrayList<>();
    }


    public ImageModel() {
        this.originalImage = null; // Vous devez obtenir l'image d'une autre manière, car elle n'est pas fournie ici
        this.perspectiveList = new ArrayList<>(); // Initialisez la liste
        perspectiveList.add(perspective1);
        perspectiveList.add(perspective2);
        this.observers = new ArrayList<>(); // Assurez-vous d'initialiser cette liste également
    }

    public Image getOriginalImage() {
        return this.originalImage;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    public void addPerspective(Perspective perspective) {
        perspectiveList.add(perspective);
    }

    public void removePerspective(int index) {
        Perspective perspectiveToRemove = null;

        for (Perspective perspective : perspectiveList) {
            if (perspective.getIndex() == index) {
                perspectiveToRemove = perspective;
            }
        }

        if (perspectiveToRemove != null) {
            perspectiveList.remove(perspectiveToRemove);
        }
    }

    public Perspective getCurrentPerspective(int index) {
        for (Perspective perspective : perspectiveList) {
            if (perspective.getIndex() == index) {
                return perspective;
            }
        }

        return null;
    }

    public int getPerspectiveIndex(Perspective perspective) {
        for (int i = 0; i < perspectiveList.size(); i++) {
            if (perspectiveList.get(i) == perspective) {
                return i + 1;
            }
        }
        return -1;
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

    // Same function twice fix this
    public Perspective getPerspective(int index) {
       return perspectiveList.get(index);
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void modifyScalePerspective(int index, boolean zoomIn, double zoomFactor, double mouseX, double mouseY) {
        Perspective perspective = getCurrentPerspective(index);
        if (perspective != null) {
            double scaleFactor = zoomIn ? zoomFactor : (1.0 / zoomFactor);

            // Obtenir les dimensions de l'image et les facteurs de zoom actuels
            double imageWidth = perspective.getImageView().getFitWidth();
            double imageHeight = perspective.getImageView().getFitHeight();
            double currentScaleX = perspective.getScaleX();
            double currentScaleY = perspective.getScaleY();

            // Calculer les nouveaux facteurs de zoom
            double newScaleX = currentScaleX * scaleFactor;
            double newScaleY = currentScaleY * scaleFactor;

            // Calculer les coordonnées de la souris par rapport à l'image
            double mouseXInImage = (mouseX - perspective.getImageView().getLayoutX()) / currentScaleX;
            double mouseYInImage = (mouseY - perspective.getImageView().getLayoutY()) / currentScaleY;

            // Calculer les nouveaux décalages
            double deltaX = perspective.getImageView().getTranslateX() - mouseXInImage * (newScaleX - currentScaleX);
            double deltaY = perspective.getImageView().getTranslateY() - mouseYInImage * (newScaleY - currentScaleY);

            // Appliquer les nouveaux facteurs de zoom et décalages
            perspective.setScaleX(newScaleX);
            perspective.setScaleY(newScaleY);
            perspective.getImageView().setTranslateX(deltaX);
            perspective.getImageView().setTranslateY(deltaY);

            notifyObservers();
        }
    }





    public void modifyTranslationPerspective(int index, double[] data, double deltaX, double deltaY) {
        Perspective perspective = getCurrentPerspective(index);
        perspective.translate(data, deltaX, deltaY);
        notifyObservers();
    }

    public Memento saveToMemento(int index, double[] imageViewData) {
        Memento memento = new Memento(index, imageViewData);
        Perspective currentPerspective = getCurrentPerspective(index);
        memento.setPerspective(index, currentPerspective);
        return memento;
    }

    public void restoreFromMemento(Memento m, int index) {
        double[] imageViewData = m.getImageViewData(index);
        if (imageViewData != null) {
            System.out.println(imageViewData[0] + " " + imageViewData[1]);
            Perspective newPerspective = m.getPerspective(index);
            setCurrentPerspective(index, newPerspective);
            // Vous devez vérifier si imageViewData a une longueur suffisante avant d'accéder à ses éléments
            if (imageViewData.length >= 4) {
                newPerspective.getImageView().setTranslateX(imageViewData[2]);
                newPerspective.getImageView().setTranslateY(imageViewData[3]);
            }
        }
    }
}
