package com.example.laboratoire_5;

public class ZoomInCommand implements Command {
    private ImageModel model;
    private Perspective previousState;
    float zoomfactor=1;
    @Override
    public void execute() {
        //augmente de 10% par click
        zoomfactor+=0.1;
        model.modifyScaleCurrentPerspective(zoomfactor);
    }
}
