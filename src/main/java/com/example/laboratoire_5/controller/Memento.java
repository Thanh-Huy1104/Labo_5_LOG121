package com.example.laboratoire_5.controller;

/**
 * Cette classe permet d'implémenter le patron Memento. En tant que memento, son but principal est simplement de stocker
 * l'état du model lorsque l'on veut. Par la suite, cet état stocké pourra être récupéré par le model pour effectuer un
 * undo ou un redo. Les mementos possèdent tous un String représentant si l'action stockée dans le memento est une
 * translation ou un zoom.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class Memento {
    private String action;

    // Data nécessaire à stocker pour une translation
    private double[] imageViewData1;
    private double[] imageViewData2;

    // Data nécessaire à stocker pour un zoom
    private double[] scales1;
    private double[] scales2;

    // CONSTRUCTOR
    public Memento(String action){
        this.action = action;
    }

    /**
     * Cette méthode fait une sauvegarde de l'état d'une translation lorsque le model veut sauvegarder son état après
     * une translation
     *
     * @param index l'index différencie les deux perspectives
     * @param imageViewData le data que l'on veut stocker
     */
    public void setImageViewData(int index, double[] imageViewData) {
        if (index == 1) {
            this.imageViewData1 = imageViewData.clone();
        } else if (index == 2) {
            this.imageViewData2 = imageViewData.clone();
        }
    }

    /**
     * Cette méthode permet d'obtenir les informations sauvegardées lorsque l'on veut faire un undo d'une translation.
     *
     * @param index l'index différencie les deux perspectives
     * @return le data stocké
     */
    public double[] getImageViewData(int index) {
        if (index == 1) {
            return imageViewData1;
        } else if (index == 2) {
            return imageViewData2;
        }
        return null;
    }

    /**
     * Cette méthode permet d'obtenir les informations sauvegardées lorsque l'on veut faire un undo d'un zoom.
     *
     * @param index l'index différencie les deux perspectives
     * @return le data stocké
     */
    public double[] getScales(int index) {
        if (index == 1) {
            return scales1;
        } else if (index == 2) {
            return scales2;
        }
        return null;
    }

    /**
     * Cette méthode fait une sauvegarde de l'état d'un zoom lorsque le model veut sauvegarder son état après
     * un zoom
     *
     * @param index l'index différencie les deux perspectives
     * @param scales le data que l'on veut stocker
     */
    public void setScales(int index, double[] scales) {
        if (index == 1) {
            this.scales1 = scales;
        } else if (index == 2) {
            this.scales2 = scales;
        }
    }

    // GETTER
    public String getAction() {
        return action;
    }
}