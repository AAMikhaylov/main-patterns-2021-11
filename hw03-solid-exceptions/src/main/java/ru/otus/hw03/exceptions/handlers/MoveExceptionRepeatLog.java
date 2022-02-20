package ru.otus.hw03.exceptions.handlers;

import ru.otus.hw03.commands.Command;
import ru.otus.hw03.commands.LogCommand;
import ru.otus.hw03.commands.RepeatCommand;
import ru.otus.hw03.ioc.IoC;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;


public class MoveExceptionRepeatLog implements Command {
    private final Command command;
    private final Exception exception;

    public MoveExceptionRepeatLog(Command command, Exception exception) {
        this.command = command;
        this.exception = exception;
    }
    @Override
    public void execute() {
        var queue = IoC.<BlockingQueue<Command>>resolve("Command.Queue");
        if (command instanceof RepeatCommand) {
            Command logCommand = new LogCommand(Level.SEVERE, command.getClass().getSimpleName() + " error. Reason: " + exception.getMessage());
            queue.add(logCommand);
        } else {
            Command repeatCommand = new RepeatCommand(command);
            queue.add(repeatCommand);
        }
    }
}
