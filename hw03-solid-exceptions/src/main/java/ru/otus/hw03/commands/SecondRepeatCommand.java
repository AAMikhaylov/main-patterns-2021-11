package ru.otus.hw03.commands;

public class SecondRepeatCommand implements Command {
    private final Command command;

    public SecondRepeatCommand(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {
        command.execute();
    }
}
