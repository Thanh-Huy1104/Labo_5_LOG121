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
    public void display(Perspective perspective) {

        setPerspective(perspective);
        ImageView imageView = perspective.getImageView();

//        imageView.setScaleX(perspective.getScaleX());
//        imageView.setScaleY(perspective.getScaleY());

        imageView.setTranslateX(perspective.getTranslationX());
        imageView.setTranslateY(perspective.getTranslationY());

        System.out.println("ImageView set to translate : " + perspective.getTranslationX() + " X   and    " + perspective.getTranslationY() + " Y");
    }
}
