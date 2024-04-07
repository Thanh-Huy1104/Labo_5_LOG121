package com.example.laboratoire_5;

public class Memento {
    private String action;
    private double[] imageViewData1;
    private double[] imageViewData2;
    private double[] scales1;
    private double[] scales2;

    public Memento(String action){
        this.action = action;
    }

    public void setImageViewData(int index, double[] imageViewData) {
        if (index == 1) {
            this.imageViewData1 = imageViewData.clone();
        } else if (index == 2) {
            this.imageViewData2 = imageViewData.clone();
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

    public double[] getScales(int index) {
        if (index == 1) {
            return scales1;
        } else if (index == 2) {
            return scales2;
        }
        return null;
    }

    public void setScales(int index, double[] scales) {
        if (index == 1) {
            this.scales1 = scales;
        } else if (index == 2) {
            this.scales2 = scales;
        }

        System.out.println("ScaleX saved : " + scales[0] + " ScaleY saved : " + scales[1]);
    }

    public String getAction() {
        return action;
    }
}
