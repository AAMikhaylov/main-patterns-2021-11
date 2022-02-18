package ru.otus.hw03.actions;

import ru.otus.hw03.QueueThread;
import ru.otus.hw03.commands.Command;
import ru.otus.hw03.commands.StopSoftCommand;

import java.util.concurrent.BlockingQueue;

public class QueueControl implements QueueControlable {
    private final QueueThread processingThread;
    private final BlockingQueue<Command> commandQueue;


    public QueueControl(QueueThread queueThread, BlockingQueue<Command> commandQueue) {
        this.processingThread = queueThread;
        this.commandQueue = commandQueue;
    }

    @Override
    public void start() {
        processingThread.start();
    }

    @Override
    public void softStop() {
        if (commandQueue.isEmpty()) {
            processingThread.stop();
            commandQueue.add(new StopSoftCommand(this));
        } else {
            if (commandQueue.stream().noneMatch(x -> x instanceof StopSoftCommand))
                commandQueue.add(new StopSoftCommand(this));
        }
    }

    @Override
    public void hardStop() {
        processingThread.hardStop();
    }

}
