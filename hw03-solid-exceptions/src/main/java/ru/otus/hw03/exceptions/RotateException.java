package ru.otus.hw03.exceptions;

public class RotateException extends RuntimeException {
    public RotateException() {
    }

    public RotateException(String message) {
        super(message);
    }

    public RotateException(String message, Throwable cause) {
        super(message, cause);
    }
}
