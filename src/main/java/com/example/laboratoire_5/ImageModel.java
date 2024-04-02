package com.example.laboratoire_5;

import javafx.scene.image.ImageView;

import java.util.List;

public class ImageModel implements Subject {

    private Image originalImage;
    private List<Perspective> perspectiveList;
    private Perspective perspective1;
    private Perspective perspective2;
    private List<Observer> observers;
    private CareTaker careTaker1;
    private CareTaker careTaker2;

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

    public void setCurrentPerspective(int index, Perspective newP) {
        for (Perspective perspective : perspectiveList) {
            if (perspective.getIndex() == index) {
                perspective = newP;
            }
        }
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    // Same function twice fix this
    public Perspective getPerspective(int index) {
        for (Perspective perspective : perspectiveList) {
            if (perspective.getIndex() == index) {
                return perspective;
            }
        }

        return null;
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    //pas sure
    public void modifyScalePerspective(float scale, int index) {
        getCurrentPerspective(perspectiveList.size()-1).setScale(scale);
    }

    // no need for indexes here coz we know which perspective scales and which translates
    public void modifyTranslationPerspective(int index, double[] data, double deltaX, double deltaY) {
        Perspective perspective = getPerspective(index);
        perspective.translate(data, deltaX, deltaY);
        notifyObservers();
    }

    public Memento createMemento() {
        Memento memento = new Memento();
        memento.setPerspective(1, getCurrentPerspective(1));
        memento.setPerspective(2, getCurrentPerspective(2));

        return memento;
    }

    public void setMemento(Memento m, int index) {
        setCurrentPerspective(index, m.getPerspective(index));
    }
}
