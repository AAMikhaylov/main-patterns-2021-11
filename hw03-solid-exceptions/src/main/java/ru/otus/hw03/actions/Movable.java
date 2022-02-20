package ru.otus.hw03.actions;

import ru.otus.hw03.util.Vector;

public interface Movable {
    Vector getPosition();

    void setPosition(Vector position);

    Vector getVelocity();
}
