package com.example.laboratoire_5;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class OriginalView implements View {

    private Perspective perspective;

    public OriginalView(Perspective perspective) {
        this.perspective = perspective;
    }
    @Override
    public void display(ImageView imageView) {} // pas besoin de faire de changement pour cette vue, car elle ne change jamais

    @Override
    public Perspective getPerspective() {
        return null;
    }
}
