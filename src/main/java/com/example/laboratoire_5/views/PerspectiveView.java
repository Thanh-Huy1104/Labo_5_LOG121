package com.example.laboratoire_5.views;

import com.example.laboratoire_5.model.Perspective;
import javafx.scene.image.ImageView;

/**
 * Cette classe permet un modèle MVC logique. Elle représente toutes les vues possédant des perspectives modifiables.
 * Chaque vue possède une perspective différente pour que les perspectives puissent être indépendantes. Cette classe
 * est une vue, donc elle implémente l'interface View. Ceci veut dire qu'elle implémente une méthode pour se mettre à
 * jour lorsque sa perspective est modifiée.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class PerspectiveView implements View {

    private Perspective perspective; // perspective associée à la vue

    // CONSTRUCTOR
    public PerspectiveView(Perspective perspective) {
        this.perspective = perspective;
    }

    // SETTER
    public void setPerspective(Perspective perspective) {
        this.perspective = perspective;
    }

    // GETTER
    public Perspective getPerspective() {
        return perspective;
    }

    /**
     * Méthode de mise à jour de la vue. Elle met à jour sa perspective. Puis, elle applique les nouveaux paramètres
     * de sa perspective au imageView. Le imageView est la vue que l'utilisateur peut observer.
     *
     * @param perspective Nouvelle perspective qui a pu être translatée ou qui a pu subir un zoom
     */
    @Override
    public void display(Perspective perspective) {

        setPerspective(perspective); // mise à jour de la perspective pour la nouvelle perspective ayant subie une action
        ImageView imageView = perspective.getImageView();

        // modification du zoom
        imageView.setScaleX(perspective.getScaleX());
        imageView.setScaleY(perspective.getScaleY());

        // modification de la translation
        imageView.setTranslateX(perspective.getTranslationX());
        imageView.setTranslateY(perspective.getTranslationY());
    }
}
