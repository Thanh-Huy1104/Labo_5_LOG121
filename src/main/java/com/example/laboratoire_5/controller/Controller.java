package com.example.laboratoire_5.controller;

import com.example.laboratoire_5.model.ImageSerializer;
import com.example.laboratoire_5.observer.Observer;
import com.example.laboratoire_5.observer.Subject;
import com.example.laboratoire_5.model.ImageModel;
import com.example.laboratoire_5.model.Perspective;
import com.example.laboratoire_5.views.OriginalView;
import com.example.laboratoire_5.views.PerspectiveView;
import com.example.laboratoire_5.views.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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


/**
 * Cette classe permet d'avoir un modèle MVC logique. Ceci est la classe controller. Son utilité est de lié les vues
 * au model et aux différentes commandes possibles dans l'application. Cette classe possède une liste de vues. Lorsque
 * l'utilisateur fait une action sur une vue, c'est dans cette classe que la gestion initiale de l'action est faite. Le
 * contrôleur peut par la suite demander d'effectuer une certaine action ou d'effectuer un undo sur une des perspectives.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class Controller implements Observer {
    public static final double ZOOM_FACTOR = 1.2; // Constante du facteur de zoom

    @FXML
    private ImageView perspective_1;

    @FXML
    private ImageView original_image;

    @FXML
    private ImageView perspective_2;

    private ImageModel model;
    private CommandManager commandManager;
    private List<View> views;
    private Map<Integer, CareTaker> careTakers; // Hash Map pour la gestion des caretakers. Chaque perspective a un caretaker
    private Integer scrollCounter = 0; // ceci est utilisé pour gérer le zoom

    /**
     * La méthode initialize est la base de toute notre application. Tous les éléments ayant besoin d'être instanciés
     * sont instanciés ici. Toute cette initialisation est effectuée dès que l'application est lancée.
     */
    @FXML
    private void initialize() {
        // Créer les perspectives associées aux imageViews
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
        this.commandManager = CommandManager.getInstance(); // acquisition de l'instance singleton

        // Gestion initiale des caretakers pour pouvoir revenir au point initial avec le patron memento
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

        // Gestion de la sauvegarde de l'image lors de la serialization
        model.setImagePath(original_image.getImage().getUrl());

        // Configurer les actions listeners des imageView pour ce qui est des translations
        setupDrag(perspective_1);
        setupDrag(perspective_2);
    }

    /**
     * Cette méthode gère les actions listeners des imageViews pour une TRANSLATION. De cette façon, dès qu'une action
     * est effectuée sur une image, le contrôleur peut directement s'en occuper.
     *
     * @param imageView imageView que l'on veut initialiser
     */
    private void setupDrag(ImageView imageView) {
        AtomicBoolean isDragging = new AtomicBoolean(false);
        double dragThreshold = 5.0; // distance minimum à parcourir pour être considéré comme une translation

        // Lorsque l'utilisateur clique pour débuter sa translation, on sauvegarde quelques éléments à propos de l'image
        imageView.setOnMousePressed(event -> {
            isDragging.set(false);
            imageView.setUserData(new double[]{event.getSceneX(), event.getSceneY(), imageView.getTranslateX(), imageView.getTranslateY()});
        });

        // Durant la translation, on gère l'action en effectuant la commande de translation à travers handleTranslate
        imageView.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX();
            double deltaY = event.getSceneY();
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            if (distance > dragThreshold) {
                isDragging.set(true);
            }
            handleTranslate(imageView, deltaX, deltaY);
        });

        // Lorsque la translation est finie, on sauvegarde l'état du model dans un caretaker
        imageView.setOnMouseReleased(event -> {
            if (isDragging.get()) {
                int index = 0;

                // code permettant d'obtenir l'index de la perspective qui a été affectée par une action
                for (View view : views) {
                    if (view instanceof PerspectiveView) {
                        if (view.getPerspective().getImageView().equals(imageView)) {
                            index = views.indexOf(view) + 1;
                        }
                    }
                }

                careTakers.get(index).savePerspective(index, "Translation"); // sauvegarde de l'état pour undo

                // sauvegarde de l'état le plus récent pour permettre un redo
                imageView.setUserData(new double[]{event.getSceneX(), event.getSceneY(), imageView.getTranslateX(), imageView.getTranslateY()});
                careTakers.get(index).saveFirstRedoPerspective(index, "Translation");
            }
        });
    }

    // Méthode ajoutant une view à la liste du controller
    public void addView(View view) {
        this.views.add(view);
    }

    /**
     * Cette méthode gère l'action listener des imageViews pour ce qui est d'un ZOOM. Lorsque l'utilisateur fait une
     * action de zoom, on la gère ici.
     *
     * @param event évènement créé par l'utilisateur
     */
    @FXML
    private void handleZoomPerspective(ScrollEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        int index = 0;
        double deltaY = event.getDeltaY();
        boolean zoomIn = deltaY < 0; // Pour savoir si le zoom est in ou out

        ZoomCommand zoomCommand = new ZoomCommand(model, zoomIn); // création de la commande vouluw
        commandManager.setCommand(zoomCommand);

        // code permettant d'obtenir l'index, puis on exécute la commande voulue
        for (View view : views) {
            if (view instanceof PerspectiveView) {
                if (view.getPerspective().getImageView().equals(imageView)) {
                    index = views.indexOf(view) + 1;
                    commandManager.executeCommand(index);
                }
            }
        }


        // code nous permettant de savoir quand l'action de zoom de l'utilisateur est réellement finie. Nous
        // considérons que si l'image est immobile pour 500ms, le zoom est fini et donc nous sauvegardons son état dans
        // un caretaker
        scrollCounter++;
        AtomicInteger newIndex = new AtomicInteger(index);
        Thread th = new Thread(() -> {
            try {
                Thread.sleep(500);
                if (scrollCounter == 1) {
                    careTakers.get(newIndex.get()).savePerspective(newIndex.get(), "Zoom"); // Sauvegarde de l'état

                    // Mise à jour des scales de la perspective
                    Perspective perspective = model.getCurrentPerspective(newIndex.get());
                    perspective.updateScales();

                    // Sauvegarde de l'état le plus récent pour permettre un redo
                    careTakers.get(newIndex.get()).saveFirstRedoPerspective(newIndex.get(), "Zoom");
                }

                scrollCounter--;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        th.setDaemon(true);
        th.start();
    }

    /**
     * Cette méthode permet la gestion d'une translation lorsque le action listener détecte une translation.
     *
     * @param imageView image sur laquelle la translation est effectuée
     * @param deltaX Variation en X
     * @param deltaY Variation en Y
     */
    private void handleTranslate(ImageView imageView, double deltaX, double deltaY) {
        double[] data = (double[]) imageView.getUserData();
        TranslationCommand translateCommand = new TranslationCommand(model, deltaX, deltaY, data);
        commandManager.setCommand(translateCommand);

        // Code permettant d'obtenir l'index et d'exécuter la commande de translation
        for (View view : views) {
            if (view instanceof PerspectiveView) {
                if (view.getPerspective().getImageView().equals(imageView)) {
                    commandManager.executeCommand(views.indexOf(view) + 1);
                }
            }
        }
    }

    // Méthode représentant le action listener du bouton UNDO de la perspective1
    @FXML
    void undoPerspective1() {
        undo(1);
    }

    // Méthode représentant le action listener du bouton UNDO de la perspective1
    @FXML
    void undoPerspective2() {
        undo(2);
    }

    /**
     * Méthode permettant de faire un UNDO. Cette méthode déclenche la séquence du patron memento afin de récupérer
     * l'ancien état du model.
     *
     * @param index l'index permet de différencier les deux perspectives
     */
    public void undo(int index) {
        CareTaker careTaker = careTakers.get(index); // obtention du bon careTaker
        if (careTaker != null) {
            careTaker.getLastMemento(index); // méthode créant le undo
        }
    }

    // Méthode représentant le action listener du bouton REDO de la perspective1
    @FXML
    void redoPerspective1(ActionEvent event) {
        redo(1);
    }

    // Méthode représentant le action listener du bouton REDO de la perspective1
    @FXML
    void redoPerspective2(ActionEvent event) {
        redo(2);
    }

    /**
     * Méthode permettant de faire un REDO. Cette méthode déclenche la séquence du patron memento afin de récupérer
     * l'ancien état du model.
     *
     * @param index l'index permet de différencier les deux perspectives
     */
    public void redo(int index) {
        CareTaker careTaker = careTakers.get(index); // obtention du bon careTaker
        if (careTaker != null) {
            careTaker.redoLastMemento(index); // méthode pour effectuer un redo
        }
    }

    // Méthode représentant l'action listener du menu item save model
    @FXML
    void saveModel() {
        ImageSerializer.serializeImageModel(this.model); // méthode pour serializer le model
    }

    // Méthode représentant l'action listener du menu item load model
    @FXML
    void loadModel() {
        ImageModel loadedModel = ImageSerializer.deserializeImageModel(); // méthode pour récupérer un model sauvegardé

        // mise à jour du nouveau model et re-initialisation des éléments non serializés
        if (loadedModel != null) {
            this.model = loadedModel;
            restoreModel(model);
        }
    }

    /**
     * Méthode qui permet de re-initialiser les éléments du nouveau model récupéré. Certains éléments ne peuvent être
     * sérializés et certains éléments doivent être ré-initialisés vu que nous avons un nouveau model (par exemple les
     * caretakers)
     *
     * @param model nouveau model récupéré
     */
    private void restoreModel(ImageModel model) {
        // Nouvelle initialisation de observer
        model.setObservers();
        model.attach(this);

        // Nouvelle initialisation des careTakers avec le nouveau model
        CareTaker careTakerPerspective1 = new CareTaker(model);
        CareTaker careTakerPerspective2 = new CareTaker(model);
        careTakers = new HashMap<>();
        careTakers.put(1, careTakerPerspective1);
        careTakers.put(2, careTakerPerspective2);
        perspective_1.setUserData(new double[]{0, 0, perspective_1.getTranslateX(), perspective_1.getTranslateY()});
        perspective_2.setUserData(new double[]{0, 0, perspective_2.getTranslateX(), perspective_2.getTranslateY()});
        Image image = new Image(model.getImagePath());

        // Gestion si l'image de base est différente de l'ancien model
        perspective_1.setImage(image);
        perspective_2.setImage(image);
        original_image.setImage(image);
        model.setOriginalImage(image, model.getImagePath());
        model.updateModel(perspective_1, perspective_2); // demande au model de s'initialiser
    }

    /**
     * Méthode update implémentée par le patron OBSERVER. Cette méthode s'assure que toutes les vues se mettent à jour
     * lorsque le model change.
     *
     * @param subject le model qui est observé par le controller
     */
    @Override
    public void update(Subject subject) {
        ImageModel model = (ImageModel) subject;
        views.get(0).display(model.getCurrentPerspective(1));
        views.get(1).display(model.getCurrentPerspective(2));
    }

    /**
     * Méthode gérant la mise en place d'une nouvelle image à modifier
     */
    @FXML
    void upload_image() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg, *.gif)", "*.png", "*.jpg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage stage = (Stage) original_image.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        // Si la nouvelle image sélectionnée est valide, on met à jour le model avec cette nouvelle image
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            original_image.setImage(image);
            perspective_1.setImage(image);
            perspective_2.setImage(image);
            model.setOriginalImage(image, file.toURI().toString());
        }
    }
}