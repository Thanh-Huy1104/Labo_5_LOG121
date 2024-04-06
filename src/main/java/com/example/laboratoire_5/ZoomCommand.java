package com.example.laboratoire_5;

public class ZoomCommand implements Command {
    private ImageModel model;
    private double zoomFactor;
    private boolean zoomIn;

    public ZoomCommand(ImageModel model, double zoomFactor, boolean zoomIn) {
        this.model = model;
        this.zoomFactor = zoomFactor;
        this.zoomIn = zoomIn;
    }

    @Override
    public void execute(int index) {
        model.modifyScalePerspective(index, zoomIn, zoomFactor);
    }
}


