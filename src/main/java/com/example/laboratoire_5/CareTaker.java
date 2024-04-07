package com.example.laboratoire_5;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class CareTaker {
    private Stack<Memento> mementos;
    private Stack<Memento> redoMementos;
    private ImageModel model;

    public CareTaker(ImageModel model) {
        mementos = new Stack<>();
        redoMementos = new Stack<>();
        this.model = model;
    }

    public void getLastMemento(int index) {
       if (!mementos.isEmpty()) {
           Memento memento = mementos.pop();
           model.restoreFromMemento(memento, index);
           redoMementos.push(memento);
       }
    }

    public void savePerspective(int index, String action) {
        Memento memento = model.saveToMemento(index, action);
        mementos.push(memento);
        redoMementos.clear();
    }

    public void redoLastMemento(int index) {
        if (!redoMementos.isEmpty()) {
            Memento redoMemento = redoMementos.pop();
            model.restoreFromMemento(redoMemento, index);
            mementos.push(redoMemento);
        }
        else System.out.println("no momento for redo");
    }
}
