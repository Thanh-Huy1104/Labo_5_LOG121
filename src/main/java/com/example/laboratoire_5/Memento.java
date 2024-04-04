package com.example.laboratoire_5;

public class Memento {
    private Perspective historyPerspective1;
    private Perspective historyPerspective2;

    public Memento(){}

    public Perspective getPerspective(int index) {
        if (index == 1) {
            return historyPerspective1;
        } else if (index == 2) {
            return historyPerspective2;
        }
        return null;
    }

    public void setPerspective(int index, Perspective newP) {
        if (index == 1) {
            historyPerspective1 = newP;
        } else if (index == 2) {
            historyPerspective2 = newP;
        }
    }
}
