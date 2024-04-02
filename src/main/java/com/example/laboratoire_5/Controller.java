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
        setupZoomAndDrag(perspective_1);
        setupZoomAndDrag(perspective_2);
    }

    private void setupZoomAndDrag(ImageView imageView) {
        imageView.setOnScroll(event -> {
            double deltaY = event.getDeltaY();
            double scale = imageView.getScaleX();
            if (deltaY < 0) {
                scale -= 0.1;
            } else {
                scale += 0.1;
            }
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
        });

        imageView.setOnMousePressed(event -> {
            imageView.setUserData(new double[]{event.getSceneX(), event.getSceneY(), imageView.getTranslateX(), imageView.getTranslateY()});
        });

        imageView.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX();
            double deltaY = event.getSceneY();
            handleTranslate(imageView ,deltaX, deltaY);
        });

    }

    public Controller() {
        this.commandManager = CommandManager.getInstance();
        this.views = new ArrayList<>();
    }

    public void addView(View view) {
        this.views.add(view);
    }

    @FXML
    private void handleZoomIn(ActionEvent event) {
        ZoomInCommand zoomInCommand = new ZoomInCommand();
    }

    @FXML
    private void handleZoomOut(ActionEvent event) {
        ZoomOutCommand zoomOutCommand = new ZoomOutCommand();
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

    }
}