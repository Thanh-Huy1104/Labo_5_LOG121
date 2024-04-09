package com.example.laboratoire_5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class Controller implements Observer {
    public static final double ZOOM_FACTOR = 1.2;

    @FXML
    private ImageView perspective_1;

    @FXML
    private ImageView original_image;

    @FXML
    private ImageView perspective_2;

    private ImageModel model;

    private CommandManager commandManager;

    private List<View> views;

    private Map<Integer, CareTaker> careTakers;

    private Integer scrollCounter = 0;

    @FXML
    private void initialize() {

        // Créer les perspectives et les vues
        Perspective perspective1 = new Perspective(1, perspective_1);
        Perspective perspective2 = new Perspective(2, perspective_2);
        Perspective originalPerspective = new Perspective(3, original_image);

        // Créer le modèle et ajouter les perspectives
        this.model = new ImageModel(perspective1, perspective2);
        model.addPerspective(perspective1);
        model.addPerspective(perspective2);
        model.addPerspective(originalPerspective);

        //Création des caretakers
        CareTaker careTakerPerspective1 = new CareTaker(model);
        CareTaker careTakerPerspective2 = new CareTaker(model);
        careTakers = new HashMap<>();
        careTakers.put(1, careTakerPerspective1);
        careTakers.put(2, careTakerPerspective2);

        // Définir la perspective actuelle sur la première perspective
        model.setCurrentPerspective(1, perspective1);
        model.setCurrentPerspective(2, perspective2);

        // Attacher le contrôleur en tant qu'observateur du modèle
        model.attach(this);

        // Créer les vues des perspectives
        PerspectiveView perspectiveView1 = new PerspectiveView(perspective1);
        PerspectiveView perspectiveView2 = new PerspectiveView(perspective2);
        OriginalView originalView = new OriginalView(originalPerspective);

        // Configurer le gestionnaire de commandes
        this.commandManager = CommandManager.getInstance();
        perspective_1.setUserData(new double[]{0, 0, 0, 0});
        perspective_2.setUserData(new double[]{0, 0, 0, 0});
        careTakerPerspective1.savePerspective(1, "Translation");
        careTakerPerspective2.savePerspective(2, "Translation");
        perspective1.updateScales();
        perspective2.updateScales();

        // Ajouter les vues à la liste de vues
        this.views = new ArrayList<>();
        addView(perspectiveView1);
        addView(perspectiveView2);
        addView(originalView);
        model.setImagePath(original_image.getImage().getUrl());

        // Initialiser la première perspective à la position et à l'échelle d'origine
        double originalImageWidth = original_image.getImage().getWidth();
        double originalImageHeight = original_image.getImage().getHeight();
        double originalImageScale = 1.0; // L'échelle initiale est 1.0
        double originalImageTranslateX = 0.0; // La translation initiale est 0.0
        double originalImageTranslateY = 0.0; // La translation initiale est 0.0

        // Mettre à jour les valeurs d'échelle et de translation si l'image d'origine existe
        if (originalImageWidth > 0 && originalImageHeight > 0) {
            // Calculer l'échelle pour que l'image occupe toute la taille de l'ImageView
            double viewWidth = perspective_1.getFitWidth();
            double viewHeight = perspective_1.getFitHeight();
            double scaleX = viewWidth / originalImageWidth;
            double scaleY = viewHeight / originalImageHeight;
            originalImageScale = Math.min(scaleX, scaleY); // Utiliser l'échelle minimale

            // Centrer l'image dans l'ImageView
            originalImageTranslateX = (viewWidth - originalImageWidth * originalImageScale) / 2;
            originalImageTranslateY = (viewHeight - originalImageHeight * originalImageScale) / 2;
        }

        // Définir l'échelle et la translation pour la première perspective
//        perspective1.setScaleX(originalImageScale);
//        perspective1.setScaleY(originalImageScale);
//        perspective1.getImageView().setTranslateX(originalImageTranslateX);
//        perspective1.getImageView().setTranslateY(originalImageTranslateY);

        // Configurer le zoom et le déplacement pour les perspectives
        setupZoomAndDrag(perspective_1);
        setupZoomAndDrag(perspective_2);
    }


    private void setupZoomAndDrag(ImageView imageView) {
        AtomicBoolean isDragging = new AtomicBoolean(false);
        double dragThreshold = 5.0;

        imageView.setOnMousePressed(event -> {
            isDragging.set(false);
            imageView.setUserData(new double[]{event.getSceneX(), event.getSceneY(), imageView.getTranslateX(), imageView.getTranslateY()});
        });

        imageView.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX();
            double deltaY = event.getSceneY();
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            if (distance > dragThreshold) {
                isDragging.set(true);
            }
            handleTranslate(imageView, deltaX, deltaY);
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

                careTakers.get(index).savePerspective(index, "Translation");
            }
        });


    }

    public void addView(View view) {
        this.views.add(view);
    }

    @FXML
    private void handleZoomPerspective(ScrollEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        int index = 0;
        double deltaY = event.getDeltaY();
        boolean zoomIn = deltaY < 0; // Si deltaY est négatif, c'est un zoom arrière, sinon c'est un zoom avant

        ZoomCommand zoomCommand = new ZoomCommand(model, zoomIn);
        commandManager.setCommand(zoomCommand);
        for (View view : views) {
            if (view instanceof PerspectiveView) {
                if (view.getPerspective().getImageView().equals(imageView)) {
                    index = views.indexOf(view) + 1;
                    commandManager.executeCommand(index);

                    view.getPerspective().setZoomIn(zoomIn);
                }
            }
        }

        scrollCounter++;
        AtomicInteger newIndex = new AtomicInteger(index);
        Thread th = new Thread(() -> {
            try {
                Thread.sleep(500);
                if (scrollCounter == 1) {
                    careTakers.get(newIndex.get()).savePerspective(newIndex.get(), "Zoom");

                    //Save old scales
                    Perspective perspective = model.getCurrentPerspective(newIndex.get());
                    perspective.updateScales();
                }

                scrollCounter--;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        th.setDaemon(true);
        th.start();
    }


    private void handleTranslate(ImageView imageView, double deltaX, double deltaY) {
        double[] data = (double[]) imageView.getUserData();
        TranslationCommand translateCommand = new TranslationCommand(model, deltaX, deltaY, data);
        commandManager.setCommand(translateCommand);
        for (View view : views) {
            if (view instanceof PerspectiveView) {
                if (view.getPerspective().getImageView().equals(imageView)) {
                    commandManager.executeCommand(views.indexOf(view) + 1);
                }
            }
        }
    }

    @FXML
    void undoPerspective1() {
        undo(1);
    }

    @FXML
    void undoPerspective2() {
        undo(2);
    }


    @FXML
    void redoPerspective1(ActionEvent event) {
        redo(1);
    }

    @FXML
    void redoPerspective2(ActionEvent event) {
        redo(2);
    }

    public void redo(int index) {
        CareTaker careTaker = careTakers.get(index);
        if (careTaker != null) {
            careTaker.redoLastMemento(index);
        }
    }

    public void undo(int index) {
        CareTaker careTaker = careTakers.get(index);
        if (careTaker != null) {
            careTaker.getLastMemento(index);
        }
    }

    ;

    @FXML
    void saveModel(ActionEvent event) {
        System.out.println("Saving model23" + model.getImagePath());
        ImageSerializer.serializeImageModel(this.model);
    }

    @FXML
    void loadModel(ActionEvent event) {
        ImageModel loadedModel = ImageSerializer.deserializeImageModel();

        if (loadedModel != null) {
            this.model = loadedModel;
            restoreModel(model);
            System.out.println("Loaded model" + model.getImagePath());
        }
    }

    private void restoreModel(ImageModel model) {
        // Since observers are not serialized, we need to reattach them
        model.setObservers();
        model.attach(this);

        // The caretakers need to be instanciated with the new model
        CareTaker careTakerPerspective1 = new CareTaker(model);
        CareTaker careTakerPerspective2 = new CareTaker(model);
        careTakers = new HashMap<>();
        careTakers.put(1, careTakerPerspective1);
        careTakers.put(2, careTakerPerspective2);
        perspective_1.setUserData(new double[]{0, 0, perspective_1.getTranslateX(), perspective_1.getTranslateY()});
        perspective_2.setUserData(new double[]{0, 0, perspective_2.getTranslateX(), perspective_2.getTranslateY()});
        System.out.println("Loaded model" + model.getImagePath());
        Image image = new Image(model.getImagePath());

        perspective_1.setImage(image);
        perspective_2.setImage(image);
        original_image.setImage(image);
        model.setOriginalImage(image, model.getImagePath());
        model.updateModel(perspective_1, perspective_2);
    }

    @Override
    public void update(Subject subject) {
        ImageModel model = (ImageModel) subject;
        views.get(0).display(model.getCurrentPerspective(1));
        views.get(1).display(model.getCurrentPerspective(2));
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
            model.setOriginalImage(image, file.toURI().toString());
        }
    }
}