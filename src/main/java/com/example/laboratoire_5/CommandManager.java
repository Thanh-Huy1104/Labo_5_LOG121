package com.example.laboratoire_5;

public class CommandManager {
    private static CommandManager instance = new CommandManager();
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    private CommandManager() {}

    public static CommandManager getInstance() {
        return instance;
    }

    public void executeCommand(int index) {
        command.execute(index);
    }
}
