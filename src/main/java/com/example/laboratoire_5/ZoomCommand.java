package com.example.laboratoire_5;

public class ZoomCommand implements Command {
    private ImageModel model;
    private int index;
    private Perspective previousState;
    private double zoomFactor;
    private boolean zoomIn;

    public ZoomCommand(ImageModel model, int index, double zoomFactor, boolean zoomIn) {
        this.model = model;
        this.index = index;
        this.zoomFactor = zoomFactor;
        this.zoomIn = zoomIn;
    }

    @Override
    public void execute(int index) {
        Perspective currentPerspective = model.getPerspective(index);
        if (zoomIn) {
            currentPerspective.setScaleX(currentPerspective.getScaleX() * zoomFactor);
            currentPerspective.setScaleY(currentPerspective.getScaleY() * zoomFactor);
        } else {
            currentPerspective.setScaleX(currentPerspective.getScaleX()/zoomFactor);
            currentPerspective.setScaleY(currentPerspective.getScaleY()/zoomFactor);
        }
        // Notifie les observateurs après le changement de perspective
        model.notifyObservers();
    }
}

