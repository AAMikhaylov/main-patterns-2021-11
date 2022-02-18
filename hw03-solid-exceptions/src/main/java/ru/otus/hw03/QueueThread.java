package ru.otus.hw03;

public interface QueueThread {
    void start();

    void stop();

    void hardStop();

    void join(long ms) throws InterruptedException;

    Thread.State getState();

}
