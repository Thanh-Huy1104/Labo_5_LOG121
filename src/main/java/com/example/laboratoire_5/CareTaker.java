package com.example.laboratoire_5;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class CareTaker {
    private Stack<Memento> mementos;
    private ImageModel model;

    public CareTaker(ImageModel model) {
        mementos = new Stack<>();
        this.model = model;
    }

    //public Perspective get(int index){return historyPerspective.get(index)}
    public Memento getLastMemento() {
       if (!mementos.isEmpty()) {
           return mementos.pop();
       }
       return null;
    }

    public void savePerspective(int index, String action) {
        mementos.push(model.saveToMemento(index, action));
    }
}
