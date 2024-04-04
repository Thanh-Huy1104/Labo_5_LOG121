package com.example.laboratoire_5;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseEvent;

import java.util.PropertyPermission;

public class ZoomCommand implements Command {
    private ImageModel model;
    //private int index;
    private Perspective previousState;
    private double zoomFactor=0.1;
    private boolean zoomIn;

    double mouseX;
    double mouseY;
    public ZoomCommand(ImageModel model, double mouseX, double mouseY, boolean zoomIn/*, int index, double zoomFactor, boolean zoomIn*/) {
       this.model = model;
       this.mouseY=mouseY;
       this.mouseX=mouseX;
       this.zoomIn = zoomIn;
        /*this.index = index;
        this.zoomFactor = zoomFactor;

        */
    }
    public double getZoomFactor(){
        return zoomFactor;
    }

    public void zoomIn(Perspective p){
        Perspective currentPerspective = model.getCurrentPerspective(p.getIndex());

        if (zoomIn) {
            currentPerspective.setScaleX(currentPerspective.getScaleX() * zoomFactor);
            currentPerspective.setScaleY(currentPerspective.getScaleY() * zoomFactor);
        } else {
            currentPerspective.setScaleX(currentPerspective.getScaleX()/zoomFactor);
            currentPerspective.setScaleY(currentPerspective.getScaleY()/zoomFactor);
        }
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

    public boolean isMouseOverImage(double mouseX, double mouseY, Perspective p){
        double pX = p.getImageView().localToScene(p.getImageView().getBoundsInLocal()).getMinX();
        double pY = p.getImageView().localToScene(p.getImageView().getBoundsInLocal()).getMinY();
        double pWidth = p.getImageView().getBoundsInParent().getWidth();
        double pHeight = p.getImageView().getBoundsInParent().getHeight();

        return (mouseX >= pX && mouseX <= pX + pWidth && mouseY >= pY && mouseY <= pY + pHeight);
    }

    public void execute(){
        for(Perspective p:model.getperspectiveList()){
            if(isMouseOverImage(this.mouseX,this.mouseY, p)){
                zoomIn(p);
            }

    }

    }
}

