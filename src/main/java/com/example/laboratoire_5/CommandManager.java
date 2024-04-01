package com.example.laboratoire_5;

import java.util.List;

public class CommandManager {
    private List<Command> commands;
    private CommandManager instance;
    private CareTaker careTaker;

    public CommandManager getInstance() {
        return instance;
    }

    public void executeCommand(Command command) {
        // TODO
    }

    public Perspective undoCommand() {
        // TODO

        return null;
    }
}
