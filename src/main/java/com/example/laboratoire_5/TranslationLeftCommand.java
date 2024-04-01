package com.example.laboratoire_5;

public class TranslationLeftCommand implements Command {
    private ImageModel model;
    private Perspective previousState;
    private float deltaX;
    private float deltaY;

    @Override
    public void execute() {
        Perspective currentPerspective = model.getCurrentPerspective(1);
        deltaX = currentPerspective.getTranslationX() - 10;
        model.modifyTranslationCurrentPerspective(deltaX, currentPerspective.getTranslationY());
    }
}
