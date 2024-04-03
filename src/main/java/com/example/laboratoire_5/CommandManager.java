package com.example.laboratoire_5;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private static CommandManager instance = new CommandManager();
    private CareTaker careTaker;

    private CommandManager() {}

    public static CommandManager getInstance() {
        return instance; // instance can never be null;
    }

    // Why do we need a list of commands if this is how we execute commands?? Gotta check patron commande
    public void executeCommand(Command command, int index) {
        careTaker.savePerspective(index);
        command.execute(index);
    }

    public void setCareTaker(CareTaker careTaker) {
        this.careTaker = careTaker;
    }

    public void undoCommand(int index) {
        careTaker.getLastPerspective(index);
    }
}
