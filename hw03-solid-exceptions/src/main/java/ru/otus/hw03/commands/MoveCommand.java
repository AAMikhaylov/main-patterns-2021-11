package ru.otus.hw03.commands;

import ru.otus.hw03.actions.Movable;
import ru.otus.hw03.util.Vector;

public class MoveCommand implements Command {
    private final Movable movable;

    public MoveCommand(Movable movable) {
        this.movable = movable;
    }

    @Override
    public void execute() {
            movable.setPosition(Vector.sum(movable.getPosition(), movable.getVelocity()));
    }
}

