package com.example.laboratoire_5;

import java.util.List;

public class CareTaker {
    private List<Perspective> historyPerspective;
    private Memento memento;

    public void addPerspective(Perspective perspective) {
        historyPerspective.add(perspective);
    }

    public Perspective getLastPerspective() {
        return historyPerspective.getLast();
    }

    public Perspective getPreviousPerspective() {
        // TODO

        return null;
    }
}
