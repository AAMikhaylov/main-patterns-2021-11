package ru.otus.hw03.commands;

public class RepeatCommand implements Command {
    private final Command command;

    public RepeatCommand(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {
        command.execute();
    }
}
