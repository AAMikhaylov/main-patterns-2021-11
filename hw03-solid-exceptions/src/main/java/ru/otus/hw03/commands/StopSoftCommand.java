package ru.otus.hw03.commands;

import ru.otus.hw03.actions.QueueControlable;

public class StopSoftCommand implements Command {
    private final QueueControlable QueueControlable;

    public StopSoftCommand(QueueControlable QueueControlable) {
        this.QueueControlable = QueueControlable;
    }

    @Override
    public void execute() {
        QueueControlable.softStop();
    }
}
