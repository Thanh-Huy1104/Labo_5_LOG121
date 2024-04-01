package com.example.laboratoire_5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import java.util.List;


public class Controller implements Observer {

    @FXML
    private MenuItem menubar_upluoad;

    @FXML
    private MenuItem menuitem_saveperspective1;

    @FXML
    private MenuItem menuitem_saveperspective2;

    @FXML
    private ImageView original_image;

    @FXML
    private ImageView perspective_1;

    @FXML
    private ImageView perspective_2;

    @FXML
    private Button redo_perspective1;

    @FXML
    private Button redo_perspective2;

    @FXML
    private Button undo_perspective1;

    @FXML
    private Button undo_perspective2;

    private Button zoomIn;

    private Button zoomOut;

    private ImageModel model;

    private CommandManager commandManager;

    private List<View> views;

    @FXML
    private void handleZoomIn(ActionEvent event) {
        ZoomInCommand zoomInCommand = new ZoomInCommand();
        zoomInCommand.execute();
    }
    @FXML
    void savePerspective1(ActionEvent event) {

    }

    @FXML
    void savePerspective2(ActionEvent event) {

    }

    @FXML
    void upload_image(ActionEvent event) {

    }

    @Override
    public void update() {

    }
}