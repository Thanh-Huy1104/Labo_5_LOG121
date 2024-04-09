package com.example.laboratoire_5;

import java.util.Stack;

public class CareTaker {
    private Stack<Memento> mementos;
    private Stack<Memento> redoMementos;
    private ImageModel model;
    private Memento lastUndo;

    public CareTaker(ImageModel model) {
        mementos = new Stack<>();
        redoMementos = new Stack<>();
        this.model = model;
    }

    public void getLastMemento(int index) {
       if (!mementos.isEmpty()) {
           Memento memento = mementos.pop();
           model.restoreFromMemento(memento, index);
           lastUndo = memento;
       }
    }

    public void savePerspective(int index, String action) {
        Memento memento = model.saveToMemento(index, action);
        mementos.push(memento);
        while (redoMementos.size() > 1) {
            redoMementos.pop();
        }
    }

    public void saveFirstRedoPerspective(int index, String action) {
        Memento memento = model.saveToMemento(index, action);
        if (redoMementos.size() == 1) {
            redoMementos.pop();
            redoMementos.push(memento);
        } else {
            redoMementos.push(memento);
        }
    }

    public void redoLastMemento(int index) {
        if (!redoMementos.isEmpty()) {
            while(redoMementos.size() > 1) {
                redoMementos.pop();
            }
            Memento redoMemento = redoMementos.peek();
            model.restoreFromMemento(redoMemento, index);
            mementos.push(lastUndo);
        }
    }
}
