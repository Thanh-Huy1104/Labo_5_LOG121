package com.example.laboratoire_5;

public class TranslationCommand implements Command {
    private ImageModel model;
    private Perspective previousState;
    private double deltaX;
    private double deltaY;

    private double[] data;

    public TranslationCommand(ImageModel model, double deltaX, double deltaY, double[] data) {
        this.model = model;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.data = data;
    }

    @Override
    public void execute(int index) {
        model.modifyTranslationPerspective(index, data, deltaX, deltaY);
    }
}
