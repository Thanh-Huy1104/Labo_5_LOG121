package com.example.laboratoire_5;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private List<Command> commands;
    private static CommandManager instance = new CommandManager();
    private CareTaker careTaker;

    private CommandManager() {
        commands = new ArrayList<>();
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void removeCommand(Command command) {
        commands.remove(command);
    }

    public static CommandManager getInstance() {
        return instance; // instance can never be null;
    }

    // Why do we need a list of commands if this is how we execute commands?? Gotta check patron commande
    public void executeCommand(Command command, int index) {
        command.execute(index);
        commands.add(command);
    }

    public void undoCommand(int index) {
        careTaker.getLastPerspective(index);
    }
}
