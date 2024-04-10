package com.example.laboratoire_5.views;

import com.example.laboratoire_5.model.Perspective;

/**
 * Cette classe une interface permettant d'avoir une modèle MVC logique. Chacune des vues de notre application doit
 * implémenter cette interface et donc inclure une méthode permettant de se mettre à jour elle-même. De plus, les vues
 * doivent posséder une méthode permettant d'obtenir leur perspective.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public interface View {
    public void display(Perspective perspective); // méthode de mise à jour des vues
    public Perspective getPerspective(); // getter de la perspective

}
