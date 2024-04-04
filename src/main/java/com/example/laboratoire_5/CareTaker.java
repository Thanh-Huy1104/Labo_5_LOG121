package com.example.laboratoire_5;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CareTaker {
    private Queue<Memento> memento;
    private ImageModel model;

    public CareTaker(ImageModel model) {
        memento = new LinkedList<>();
        this.model = model;
    }

    //public Perspective get(int index){return historyPerspective.get(index)}
    public void getLastPerspective(int index) {
        model.setMemento(memento.remove(), index);
    }

    // don't think we even need this function. Elle sert a quoi?
    public Perspective getPreviousPerspective() {
        // TODO

        return null;
    }

    public void savePerspective(int index) {
        memento.add(model.createMemento(index));
    }
}
