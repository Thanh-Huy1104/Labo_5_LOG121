package com.example.laboratoire_5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


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

    private CareTaker careTaker;

    public Controller() {}

    @FXML
    private void initialize() {
        Perspective perspective1 = new Perspective(1, perspective_1);
        Perspective perspective2 = new Perspective(2, perspective_2);
        Perspective originalPerspective = new Perspective(3, original_image);
        this.model = new ImageModel(perspective1, perspective2);
        model.addPerspective(perspective1);
        model.addPerspective(perspective2);
        model.addPerspective(originalPerspective);
        model.setCurrentPerspective(1, perspective1);
        model.setCurrentPerspective(2, perspective2);
        model.attach(this);
        PerspectiveView perspectiveView1 = new PerspectiveView(perspective1);
        PerspectiveView perspectiveView2 = new PerspectiveView(perspective2);
        OriginalView originalView = new OriginalView(originalPerspective);
        this.commandManager = CommandManager.getInstance();
        this.commandManager.setImageModel(model);
        careTaker = new CareTaker(model);
        this.commandManager.setCareTaker(careTaker);
        careTaker.savePerspective(1, new double[]{0, 0, 0, 0});
        careTaker.savePerspective(2, new double[]{0, 0, 0, 0});
        this.views = new ArrayList<>();
        addView(perspectiveView1);
        addView(perspectiveView2);
        addView(originalView);
        setupZoomAndDrag(perspective_1);
        setupZoomAndDrag(perspective_2);
    }

    private void setupZoomAndDrag(ImageView imageView) {
//        imageView.setOnScroll(event -> {
//            double deltaY = event.getDeltaY();
//            double scale = imageView.getScaleX();
//            if (deltaY < 0) {
//                scale -= 0.1;
//            } else {
//                scale += 0.1;
//            }
//            imageView.setScaleX(scale);
//            imageView.setScaleY(scale);
//        });

        AtomicBoolean isDragging = new AtomicBoolean(false);
        double dragThreshold = 5.0;

        imageView.setOnMousePressed(event -> {
            isDragging.set(false);
            imageView.setUserData(new double[]{event.getSceneX(), event.getSceneY(), imageView.getTranslateX(), imageView.getTranslateY()});
        });

        imageView.setOnMouseDragged(event -> { // save in memento here? For initial state of the imageView before any translation?
            double deltaX = event.getSceneX();
            double deltaY = event.getSceneY();
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            if (distance > dragThreshold) {
                isDragging.set(true);
            }
            handleTranslate(imageView ,deltaX, deltaY);
        });

        imageView.setOnMouseReleased(event -> {
            if (isDragging.get()) {
                int index = 0;
                for (View view : views) {
                    if (view instanceof PerspectiveView) {
                        if (view.getPerspective().getImageView().equals(imageView)) {
                            index = views.indexOf(view) + 1;
                        }
                    }
                }

                careTaker.savePerspective(index, (double[]) imageView.getUserData());
            }
        });
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
    void undoPerspective1() {
        commandManager.undo(1);
    }

    @FXML
    void undoPerspective2() {
        commandManager.undo(2);
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
    public void update(Subject subject) {
        ImageModel model = (ImageModel) subject;
        views.get(0).display(model.getCurrentPerspective(1));
        views.get(1).display(model.getCurrentPerspective(2));
    }
}