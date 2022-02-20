package ru.otus.hw03.ioc;

import ru.otus.hw03.commands.Command;

import java.util.*;
import java.util.function.Function;

public class IoC {
    private final static Map<String, Function<Object[], Object>> strategies = new HashMap<>();

    private static class RegDepCommand implements Command {
        private final String key;
        private final Function<Object[], Object> strategy;

        RegDepCommand(String key, Function<Object[], Object> strategy) {
            this.key = key;
            this.strategy = strategy;
        }

        @Override
        public void execute() {
            strategies.put(key, strategy);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T resolve(String key, Object... args) {
        if (key.equals("IoC.Registry")) {
            return (T) (new RegDepCommand((String) args[0], (Function<Object[], Object>) args[1]));
        }
        var strategy = strategies.get(key);
        if (strategy != null) {
            return (T) strategies.get(key).apply(args);
        } else {
            throw new ResolveDependencyException("Can't resolve dependency: " + key);
        }
    }
}
