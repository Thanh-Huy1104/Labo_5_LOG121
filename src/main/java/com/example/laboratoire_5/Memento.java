package com.example.laboratoire_5;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Memento {
    private Queue<Perspective> historyPerspective1;
    private Queue<Perspective> historyPerspective2;

    public Memento(){
        historyPerspective1 = new LinkedList<>();
        historyPerspective2 = new LinkedList<>();
    }

    public Perspective getPerspective(int index) {
        if (index == 1) {
            return historyPerspective1.remove();
        } else if (index == 2) {
            return historyPerspective2.remove();
        }
        return null;
    }

    public void setPerspective(int index, Perspective newP) {
        if (index == 1) {
            historyPerspective1.add(newP);
        } else if (index == 2) {
            historyPerspective2.add(newP);
        }
    }
}
