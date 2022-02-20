package ru.otus.hw03.commands;

import ru.otus.hw03.actions.QueueControlable;

public class StopHardCommand implements Command {
    private final QueueControlable QueueControlable;

    public StopHardCommand(QueueControlable QueueControlable) {
        this.QueueControlable = QueueControlable;
    }

    @Override
    public void execute() {
        QueueControlable.hardStop();
    }
}
