package ru.yandex.practicum.filmorate.exception;

public class NullObjectException extends NullPointerException {
    public NullObjectException(final String message) {
        super(message);
    }
}
