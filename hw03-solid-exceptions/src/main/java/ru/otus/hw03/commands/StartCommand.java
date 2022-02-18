package ru.otus.hw03.commands;

import ru.otus.hw03.actions.QueueControlable;

public class StartCommand implements Command {
    private final QueueControlable QueueControlable;

    public StartCommand(QueueControlable QueueControlable) {
        this.QueueControlable = QueueControlable;
    }

    @Override
    public void execute() {
        QueueControlable.start();
    }
}
