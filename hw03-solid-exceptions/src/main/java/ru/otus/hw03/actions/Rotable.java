package ru.otus.hw03.actions;

public interface Rotable {
    void setDirection(int direction);

    int getDirection();

    int getMaxDirections();

    int getAngularVelocity();
}
