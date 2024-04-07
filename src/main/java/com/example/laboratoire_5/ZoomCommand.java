package com.example.laboratoire_5;

public class ZoomCommand implements Command {
    private ImageModel model;
    private boolean zoomIn;

    public ZoomCommand(ImageModel model, boolean zoomIn) {
        this.model = model;
        this.zoomIn = zoomIn;
    }

    @Override
    public void execute(int index) {
        model.modifyScalePerspective(index, zoomIn);
    }
}


