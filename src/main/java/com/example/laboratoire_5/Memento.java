package com.example.laboratoire_5;

public class Memento {
    private Perspective historyPerspective1;
    private Perspective historyPerspective2;

    private double[] imageViewData1;
    private double[] imageViewData2;

    public Memento(int index, double [] imageViewData){
        if (index == 1) {
            this.imageViewData1 = imageViewData.clone();
        } else if (index == 2) {
            this.imageViewData2 = imageViewData.clone();
        }
    }

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

    public double[] getImageViewData(int index) {
        if (index == 1) {
            return imageViewData1;
        } else if (index == 2) {
            return imageViewData2;
        }
        return null;
    }
}
