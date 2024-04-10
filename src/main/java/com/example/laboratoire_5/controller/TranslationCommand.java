package com.example.laboratoire_5.controller;

import com.example.laboratoire_5.model.ImageModel;

/**
 * Cette classe est utilisée pour l'implémentation du patron commande. Elle gère une commande concrète qui est la
 * translation. Elle implémente l'interface Command, donc elle doit contenir la méthode execute. Cette classe fait donc
 * partie de la séquence d'actions nécessaire pour effectuer une translation.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class TranslationCommand implements Command {
    private ImageModel model;
    private double deltaX;
    private double deltaY;
    private double[] data; // référence au data du imageView qui est "set" lors des actions dans le controller

    // CONSTRUCTOR
    public TranslationCommand(ImageModel model, double deltaX, double deltaY, double[] data) {
        this.model = model;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.data = data;
    }

    /**
     * Méthode implémentée de l'interface Command. Cette classe a une référence au model, qui est le receiver du patron
     * Commande, donc la méthode ne fait que dire au model d'effectuer l'action de translation. On donne également en
     * paramètres au model toutes les informations nécessaires pour effectuer la translation.
     *
     * @param index l'index permet de différencier les deux perspectives.
     */
    @Override
    public void execute(int index) {
        model.modifyTranslationPerspective(index, data, deltaX, deltaY);
    }
}
