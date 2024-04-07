package com.example.laboratoire_5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private static CommandManager instance = new CommandManager();
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    private CommandManager() {}

    public static CommandManager getInstance() {
        return instance; // instance can never be null;
    }

    public void executeCommand(int index) {
        command.execute(index);
    }

    public void redoCommand(int index) {
        // TODO
    }


}
