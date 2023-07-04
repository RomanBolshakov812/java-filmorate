package ru.yandex.practicum.filmorate.exception;

public class NegativeValueException extends RuntimeException {
    public NegativeValueException(final String message) {
        super(message);
    }
}
