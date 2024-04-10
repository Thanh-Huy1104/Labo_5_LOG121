package com.example.laboratoire_5.views;

import com.example.laboratoire_5.model.Perspective;

/**
 * Cette classe permet un modèle MVC logique. Elle représente la vue originale de l'image. Elle n'est jamais modifiée,
 * mais cette classe est tout de même une vue, donc elle implémente l'interface View. Ceci veut dire qu'elle implémente
 * une méthode pour se mettre à jour. Cette méthode n'est pas utilisée dans notre application, mais elle pourrait
 * l'être si on voulait ajouter des éléments à l'application. Il est important qu'elle possède tout de même la méthode.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class OriginalView implements View {
    private Perspective perspective;

    // CONSTRUCTOR
    public OriginalView(Perspective perspective) {
        this.perspective = perspective;
    }
    @Override
    public void display(Perspective perspective) {} // Cette vue n'est jamais modifiée
    @Override
    public Perspective getPerspective() {
        return null;
    }
}