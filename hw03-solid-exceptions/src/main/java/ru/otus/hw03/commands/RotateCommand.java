package ru.otus.hw03.commands;

import ru.otus.hw03.actions.Rotable;

public class RotateCommand implements Command {
    private final Rotable rotable;

    public RotateCommand(Rotable rotable) {
        this.rotable = rotable;
    }

    @Override
    public void execute() {
        rotable.setDirection((rotable.getDirection() + rotable.getAngularVelocity()) % rotable.getMaxDirections());
    }


}
