package ru.otus.hw03;

import ru.otus.hw03.commands.Command;
import ru.otus.hw03.ioc.IoC;

import java.util.concurrent.BlockingQueue;

public class QueueThreadImpl implements QueueThread {
    private final Thread thread;
    private final BlockingQueue<Command> queue;
    private boolean stop;

    public QueueThreadImpl(BlockingQueue<Command> queue) {
        this.queue = queue;
        this.thread = new Thread(() -> {
            try {
                while (!stop) {
                    var cmd = this.queue.take();
                    try {
                        cmd.execute();
                    } catch (Exception e) {
                        IoC.<Command>resolve(cmd.getClass().getSimpleName() + "." + e.getClass().getSimpleName(), cmd, e).execute();
                    }
                }
            } catch (InterruptedException e) {
                IoC.<Command>resolve("Queue." + e.getClass().getSimpleName(), e).execute();
            }
        });
    }

    @Override
    public void start() {
        stop = false;
        thread.start();
    }

    @Override
    public void stop() {
        stop = true;
    }
    @Override
    public void hardStop() {
        stop = true;
        thread.interrupt();
    }


    @Override
    public void join(long ms) throws InterruptedException {
        thread.join(ms);
    }

    @Override
    public Thread.State getState() {
        return thread.getState();
    }
}
