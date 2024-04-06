package com.example.laboratoire_5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class Controller implements Observer {

    @FXML
    private ImageView perspective_1;

    @FXML
    private ImageView original_image;

    @FXML
    private ImageView perspective_2;

    private ImageModel model;

    private CommandManager commandManager;

    private List<View> views;

    private CareTaker careTakerPerspective1;

    private CareTaker careTakerPerspective2;

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
        careTakerPerspective1 = new CareTaker(model);
        careTakerPerspective2 = new CareTaker(model);


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
        this.commandManager.setImageModel(model);
        this.commandManager.setCareTaker(1, careTakerPerspective1);
        this.commandManager.setCareTaker(2, careTakerPerspective2);
        careTakerPerspective1.savePerspective(1, new double[]{0, 0, 0, 0});
        careTakerPerspective2.savePerspective(2, new double[]{0, 0, 0, 0});

        // Ajouter les vues à la liste de vues
        this.views = new ArrayList<>();
        addView(perspectiveView1);
        addView(perspectiveView2);
        addView(originalView);

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
        setupZoomAndDrag(perspective_1, 1);
        setupZoomAndDrag(perspective_2, 2);
    }


    private void setupZoomAndDrag(ImageView imageView, int careTakerIndex) {
        CareTaker careTaker = CommandManager.getInstance().getCareTaker(careTakerIndex);
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

                careTaker.savePerspective(index, (double[]) imageView.getUserData());
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
        double zoomFactor = 1.2; // Facteur de zoom
        boolean zoomIn = deltaY < 0; // Si deltaY est négatif, c'est un zoom arrière, sinon c'est un zoom avant

        double mouseX = event.getX();
        double mouseY = event.getY();
        ZoomCommand zoomCommand = new ZoomCommand(model, zoomFactor, zoomIn, mouseX, mouseY);
        for (View view : views) {
            if (view instanceof PerspectiveView) {
                if (view.getPerspective().getImageView().equals(imageView)) {
                    index = views.indexOf(view) + 1;
                    commandManager.executeCommand(zoomCommand, index);

                    // Mise à jour des données utilisateur avec les nouvelles informations de zoom
                    double[] userData = new double[]{mouseX, mouseY, imageView.getScaleX(), imageView.getScaleY()};
                    imageView.setUserData(userData);
                }
            }
        }

        scrollCounter++;
        AtomicInteger newIndex = new AtomicInteger(index);
        Thread th = new Thread(() -> {
            try {
                Thread.sleep(1000);
                if (scrollCounter == 1) {
                    System.out.println("zoom fini");
                    CommandManager.getInstance().getCareTaker(newIndex.get()).savePerspective(newIndex.get(), (double[]) imageView.getUserData());
                    System.out.println(newIndex.get());
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
    void saveModel(ActionEvent event) {
        ImageSerializer.serializeImageModel(this.model);
    }

    @FXML
    void loadModel(ActionEvent event) {
        ImageModel loadedModel = ImageSerializer.deserializeImageModel();

        if (loadedModel != null) {
            this.model = loadedModel;
            restoreModel(model);
        }
    }

    private void restoreModel(ImageModel model) {
        // Since observers are not serialized, we need to reattach them
        model.setObservers();
        model.attach(this);

        model.updateModel(perspective_1, perspective_2);
    }

    @Override
    public void update(Subject subject) {
        ImageModel model = (ImageModel) subject;
        views.get(0).display(model.getCurrentPerspective(1));
        views.get(1).display(model.getCurrentPerspective(2));
    }
}