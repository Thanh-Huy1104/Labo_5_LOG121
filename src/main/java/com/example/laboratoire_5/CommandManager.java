package com.example.laboratoire_5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private static CommandManager instance = new CommandManager();
    private Map<Integer, CareTaker> careTakers;



    public ImageModel getImageModel() {
        return imageModel;
    }

    public void setImageModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    private ImageModel imageModel;

    private CommandManager() {
        careTakers = new HashMap<>(); // Initialisation de la carte
    }

    public static CommandManager getInstance() {
        return instance; // instance can never be null;
    }

    public CareTaker getCareTaker(int index) {
        return careTakers.get(index);
    }

    public void setCareTaker(int perspectiveIndex, CareTaker careTaker) {
        careTakers.put(perspectiveIndex, careTaker);
    }

    public void executeCommand(Command command, int index) {
        command.execute(index);
    }
    public void undo(int index) {
        CareTaker careTaker = careTakers.get(index);
        if (careTaker != null) {
            Memento memento = careTaker.getLastMemento();
            if (memento != null) {
                imageModel.restoreFromMemento(memento, index);
            }
        }
    };

    public void redoCommand(int index) {
        // TODO
    }


}
