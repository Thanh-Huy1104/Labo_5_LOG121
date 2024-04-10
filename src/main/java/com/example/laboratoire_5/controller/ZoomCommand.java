package com.example.laboratoire_5.controller;

import com.example.laboratoire_5.model.ImageModel;

/**
 * Cette classe est utilisée pour l'implémentation du patron commande. Elle gère une commande concrète qui est le zoom.
 * Elle implémente l'interface Command, donc elle doit contenir la méthode execute. Cette classe fait donc partie de la
 * séquence d'actions nécessaire pour effectuer un zoom.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class ZoomCommand implements Command {
    private ImageModel model;
    private boolean zoomIn; // variable permettant de savoir si l'utilisateur fait un zoomIn ou un zoomOut

    // CONSTRUCTOR
    public ZoomCommand(ImageModel model, boolean zoomIn) {
        this.model = model;
        this.zoomIn = zoomIn;
    }

    /**
     * Méthode implémentée de l'interface Command. Cette classe a une référence au model, qui est le receiver du patron
     * Commande, donc la méthode ne fait que dire au model d'effectuer l'action de zoom en lui indiquand si c'est un
     * zoom in ou un zoom out.
     *
     * @param index l'index permet de différencier les deux perspectives.
     */
    @Override
    public void execute(int index) {
        model.modifyScalePerspective(index, zoomIn);
    }
}


