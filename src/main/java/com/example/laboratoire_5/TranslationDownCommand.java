package com.example.laboratoire_5;

public class TranslationDownCommand implements Command {
    private ImageModel model;
    private Perspective previousState;
    private float deltaX;
    private float deltaY;

    @Override
    public void execute() {
        Perspective currentPerspective = model.getCurrentPerspective(1);
        deltaY = currentPerspective.getTranslationY() - 10;
        model.modifyTranslationCurrentPerspective(currentPerspective.getTranslationX(), deltaY);
    }
}
