package com.example.laboratoire_5;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private static CommandManager instance = new CommandManager();

    private CareTaker careTaker;

    public CareTaker getCareTaker() {
        return careTaker;
    }

    public void setCareTaker(CareTaker careTaker) {
        this.careTaker = careTaker;
    }

    public ImageModel getImageModel() {
        return imageModel;
    }

    public void setImageModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    private ImageModel imageModel;

    public static CommandManager getInstance() {
        return instance; // instance can never be null;
    }

    // Why do we need a list of commands if this is how we execute commands?? Gotta check patron commande
    public void executeCommand(Command command, int index) {
        command.execute(index);
    }
    public void undo(int index) {
        Memento memento = careTaker.getLastMemento();
        if (memento != null) {
            System.out.println("Undoing command");
            imageModel.restoreFromMemento(memento, index);
        }
    };

    public void redoCommand(int index) {
        // TODO
    }


}
