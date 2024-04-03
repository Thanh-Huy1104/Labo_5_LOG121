package com.example.laboratoire_5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Controller implements Observer {

    @FXML
    private MenuItem menubar_upluoad;

    @FXML
    private MenuItem menuitem_saveperspective1;

    @FXML
    private MenuItem menuitem_saveperspective2;

    @FXML
    private ImageView perspective_1;

    @FXML
    private ImageView original_image;

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

    private ImageModel model;

    private CommandManager commandManager;

    private List<View> views;

    @FXML
    private void initialize() {

        model = new ImageModel();
        model.attach(this);
    }

    public Controller() {
        this.commandManager = CommandManager.getInstance();
        this.views = new ArrayList<>();
    }

    public void addView(View view) {
        this.views.add(view);
    }


    @FXML
    void handleZoomPerspective1(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        double zoomFactor = 0.1; // Facteur de zoom
        int index = 0;
        boolean zoomIn = deltaY < 0; // Si deltaY est négatif, c'est un zoom arrière, sinon c'est un zoom avant
        ZoomCommand zoomCommand = new ZoomCommand(model, index, zoomFactor, zoomIn);
        commandManager.executeCommand(zoomCommand, index);

    }

    @FXML
    void handleZoomPerspective2(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        double zoomFactor = 0.1; // Facteur de zoom
        int index = 1;
        boolean zoomIn = deltaY < 0; // Si deltaY est négatif, c'est un zoom arrière, sinon c'est un zoom avant
        ZoomCommand zoomCommand = new ZoomCommand(model, index, zoomFactor, zoomIn);
        commandManager.executeCommand(zoomCommand, index);
    }

    private void handleTranslate(ImageView imageView, double deltaX, double deltaY) {
        double[] data = (double[]) imageView.getUserData();
        TranslationCommand translateCommand = new TranslationCommand(model, deltaX, deltaY, data);
        for (View view : views) {
            if (view instanceof PerspectiveView) {
                if (view.getPerspective().getImageView().equals(imageView)) {
                    commandManager.executeCommand(translateCommand, views.indexOf(view) + 1);
                }
            }
        }
    }

    @FXML
    void savePerspective1(ActionEvent event) {

    }

    @FXML
    void savePerspective2(ActionEvent event) {

    }

    @FXML
    void upload_image(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = (Stage) original_image.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            original_image.setImage(image);
            perspective_1.setImage(image);
            perspective_2.setImage(image);
        }
    }

    @Override
    public void update() {
        Perspective perspective1 = model.getCurrentPerspective(0); // Récupérer la perspective 1 du modèle
        Perspective perspective2 = model.getCurrentPerspective(1); // Récupérer la perspective 1 du modèle
        if (perspective1 != null) {
            // Mettre à jour l'échelle de perspective_1 en fonction de l'échelle de la perspective récupérée
            perspective_1.setScaleX(perspective1.getScale());
            perspective_1.setScaleY(perspective1.getScale());
        }

        if (perspective2 != null) {
            // Mettre à jour l'échelle de perspective_1 en fonction de l'échelle de la perspective récupérée
            perspective_2.setScaleX(perspective2.getScale());
            perspective_2.setScaleY(perspective2.getScale());
        }

    }
}