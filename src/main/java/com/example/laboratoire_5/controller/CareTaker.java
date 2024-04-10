package com.example.laboratoire_5.controller;

import com.example.laboratoire_5.model.ImageModel;
import java.util.Stack;

/**
 * Cette classe est utilisée pour l'implémentation du patron memento. Comme l'indique son nom, cette classe représente
 * l'élément careTaker du patron memento. C'est cette classe qui conserve la référence des mementos et c'est cette
 * classe qui indique au model lorsqu'il est le temps de sauvegarder ou de restituer son état.
 *
 * @author Hugo Vaillant, Thanh-Huy Nguyen, Primika Khayargoli, Yassine Graitaa
 * @version H2024
 */
public class CareTaker {
    private Stack<Memento> mementos; // Stack de mementos pour pouvoir undo plusieurs fois de suite
    private Stack<Memento> redoMementos; // Stack représentant les mementos à redo
    private ImageModel model; // Référence au model
    private Memento lastUndo; // Dernier undo effectué. Cet attribut nous permet d'implémenter le redo.

    // Constructeur nécessitant le model en paramètre
    public CareTaker(ImageModel model) {
        mementos = new Stack<>();
        redoMementos = new Stack<>();
        this.model = model;
    }


    /**
     * Cette méthode est celle responsable du undo. Elle pop le dernier état sauvegardé et indique au model de
     * revenir à cet état.
     *
     * @param index l'index permet de savoir quelle perspective est affectée par le undo
     */
    public void getLastMemento(int index) {
       if (!mementos.isEmpty()) {
           Memento memento = mementos.pop();
           model.restoreFromMemento(memento, index);
           lastUndo = memento; // ceci permet de conservé la dernière action qui a été undo
       }
    }

    /**
     * Cette méthode gère la sauvegarde de l'état du model. La fonction demande au model de créer un memento avec son
     * état actuel. Puis, on ajoute ce memento au stack.
     *
     * @param index index l'index permet de savoir l'action a été effectuée sur laquelle des perspectives
     * @param action l'action est un String que l'on associe à Translation ou à Zoom afin de différencier les deux actions
     */
    public void savePerspective(int index, String action) {
        Memento memento = model.saveToMemento(index, action);
        mementos.push(memento);

        // Il est important que le stack de redo ne possède que l'action la plus récente
        while (redoMementos.size() > 1) {
            redoMementos.pop();
        }
    }

    /**
     * Cette méthode permet de sauvegarder l'état le plus récent du model directement dans le stack de redo.
     * Ceci est nécessaire vu la manière dont nous sauvegardons les états du model. Cette méthode s'assure donc que
     * le stack de redo contient toujours l'action la plus récente que l'on voudrait refaire s'il y a un undo.
     *
     * @param index l'index permet de savoir l'action a été effectuée sur laquelle des perspectives
     * @param action l'action est un String que l'on associe à Translation ou à Zoom afin de différencier les deux actions
     */
    public void saveFirstRedoPerspective(int index, String action) {
        Memento memento = model.saveToMemento(index, action);

        // Si le stack contient déjà une action sauvegardée, on la remplace par la nouvelle action. Sinon, on ajoute l'action
        if (redoMementos.size() == 1) {
            redoMementos.pop();
            redoMementos.push(memento);
        } else {
            redoMementos.push(memento);
        }
    }

    /**
     * Cette méthode est responsable de l'action redo. Lorsque l'utilisateur veut redo, on regarde quel est l'élément
     * dans le stack de redoMementos et on demande on model de se mettre à jour selon cet état.
     *
     * @param index l'index permet de savoir l'action a été effectuée sur laquelle des perspectives
     */
    public void redoLastMemento(int index) {
        if (!redoMementos.isEmpty()) {

            // On s'assure qu'il n'y a qu'un élément dans le redo. Ceci est utile lorsque l'utilisateur clique undo et
            // redo en alternance plusieurs fois.
            while(redoMementos.size() > 1) {
                redoMementos.pop();
            }

            Memento redoMemento = redoMementos.peek();
            model.restoreFromMemento(redoMemento, index);
            mementos.push(lastUndo); // On ajoute de nouveau la dernière action qui a été pop par undo au stack de undo
        }
    }
}
