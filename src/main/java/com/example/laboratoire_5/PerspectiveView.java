package com.example.laboratoire_5;

import javafx.scene.image.ImageView;

public class PerspectiveView implements View {

    private Perspective perspective;

    public PerspectiveView(Perspective perspective) {
        this.perspective = perspective;
    }

    public void setPerspective(Perspective perspective) {
        this.perspective = perspective;
    }

    public Perspective getPerspective() {
        return perspective;
    }

    @Override
    public void display(ImageView imageView) {

        imageView.setScaleX(perspective.getScaleX());
        imageView.setScaleY(perspective.getScaleY());
    }
}
