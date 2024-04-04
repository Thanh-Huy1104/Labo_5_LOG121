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
        careTaker = new CareTaker(model);
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

        imageView.setOnMousePressed(event -> { // save in memento here? For initial state of the imageView before any translation?
            imageView.setUserData(new double[]{event.getSceneX(), event.getSceneY(), imageView.getTranslateX(), imageView.getTranslateY()});
            int index = 0;
            for (View view : views) {
                if (view instanceof PerspectiveView) {
                    if (view.getPerspective().getImageView().equals(imageView)) {
                        index = views.indexOf(view) + 1;
                    }
                }
            }
            careTaker.savePerspective(index);
        });

        imageView.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX();
            double deltaY = event.getSceneY();
            handleTranslate(imageView ,deltaX, deltaY);
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
//if mouse location is on the perspective, it will zoom into that given perspective
    void handleZoomPerspective(ScrollEvent event){
        double deltaY = event.getDeltaY();
        boolean zoomIn = deltaY < 0; // Si deltaY est négatif, c'est un zoom arrière, sinon c'est un zoom avant
        double mouseX = event.getX();
        double mouseY = event.getY();
        Command zoomCommand=new ZoomCommand(model,mouseX,mouseY,zoomIn);
        zoomCommand.execute();
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
        careTaker.getLastPerspective(1);
    }

    @FXML
    void undoPerspective2() {
        careTaker.getLastPerspective(2);
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
        views.get(0).display(perspective_1);
        views.get(1).display(perspective_2);
    }
}