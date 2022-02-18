package ru.otus.hw03.exceptions.handlers;

import ru.otus.hw03.commands.Command;
import ru.otus.hw03.commands.LogCommand;
import ru.otus.hw03.commands.SecondRepeatCommand;
import ru.otus.hw03.commands.RepeatCommand;
import ru.otus.hw03.ioc.IoC;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;

public class MoveExceptionTwoRepeatLog implements Command {
    private final Command command;
    private final Exception exception;

    public MoveExceptionTwoRepeatLog(Command command, Exception exception) {
        this.command = command;
        this.exception = exception;
    }

    @Override
    public void execute() {
        var queue = IoC.<BlockingQueue<Command>>resolve("Command.Queue");
        if (command instanceof SecondRepeatCommand) {
            Command logCommand = new LogCommand(Level.SEVERE, command.getClass().getSimpleName() + " error. Reason: " + exception.getMessage());
            queue.add(logCommand);
            return;
        }
        if (command instanceof RepeatCommand) {
            Command secondRepeatCommand = new SecondRepeatCommand(command);
            queue.add(secondRepeatCommand);
            return;
        }
        Command firstRepeatCommand = new RepeatCommand(command);
        queue.add(firstRepeatCommand);
    }
}
