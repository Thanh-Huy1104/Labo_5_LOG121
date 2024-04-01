package com.example.laboratoire_5;

import java.util.List;

public class CareTaker {
    //arraylist?
    private List<Perspective> historyPerspective;
    private Memento memento;

    public void addPerspective(Perspective perspective) {
        historyPerspective.add(perspective);
    }

    //public Perspective get(int index){return historyPerspective.get(index)}
    public Perspective getLastPerspective() {
        return historyPerspective.get(historyPerspective.size() - 1);
    }

    public Perspective getPreviousPerspective() {
        // TODO

        return null;
    }
}
