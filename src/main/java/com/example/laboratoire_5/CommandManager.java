package com.example.laboratoire_5;

import java.util.List;

public class CommandManager {
    private List<Command> commands;
    private CommandManager instance;
    private CareTaker careTaker;

    public CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    public void executeCommand(Command command) {
        command.execute();
        commands.add(command);
    }

    public Perspective undoCommand() {
        // TODO

        return null;
    }
}
