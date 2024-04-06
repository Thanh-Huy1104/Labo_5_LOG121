package com.example.laboratoire_5;

public class ZoomCommand implements Command {
    private ImageModel model;
    private double zoomFactor;
    private boolean zoomIn;
    private double mouseX;
    private double mouseY;

    public ZoomCommand(ImageModel model, double zoomFactor, boolean zoomIn, double mouseX, double mouseY) {
        this.model = model;
        this.zoomFactor = zoomFactor;
        this.zoomIn = zoomIn;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public void execute(int index) {
        model.modifyScalePerspective(index, zoomIn, zoomFactor, mouseX, mouseY);
    }
}


