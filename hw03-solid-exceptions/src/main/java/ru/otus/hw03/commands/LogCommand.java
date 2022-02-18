package ru.otus.hw03.commands;

import ru.otus.hw03.ioc.IoC;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogCommand implements Command {

    private final String message;
    private final Level logLevel;


    public LogCommand(Level logLevel, String message) {
        this.message = message;
        this.logLevel = logLevel;

    }

    @Override
    public void execute() {
        var logger = IoC.<Logger>resolve("Application.Logger");
        logger.log(logLevel, message);
    }
}
