package com.example.laboratoire_5;

import java.util.List;

public class CareTaker {
    private Memento memento;
    private ImageModel model;

    public CareTaker(ImageModel model) {
        this.model = model;
    }

    //public Perspective get(int index){return historyPerspective.get(index)}
    public void getLastPerspective(int index) {
        model.setMemento(memento, index);
    }

    // don't think we even need this function. Elle sert a quoi?
    public Perspective getPreviousPerspective() {
        // TODO

        return null;
    }

    public void savePerspective(int index) {
        this.memento = model.createMemento(index);
    }
}
