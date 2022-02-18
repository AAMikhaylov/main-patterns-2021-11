package ru.otus.hw03.exceptions;

public class MoveException extends RuntimeException {
    public MoveException() {
        super();
    }

    public MoveException(String message) {
        super(message);
    }

    public MoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
