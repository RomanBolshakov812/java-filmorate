package ru.yandex.practicum.filmorate.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NegativeValueException;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NullObjectException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNullObjectException(final NullObjectException e) {
        return new ErrorResponse("Ошибка: объект не найден!", e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse("Ошибка валидации!", e.getMessage());
    }

    @ExceptionHandler(NegativeValueException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNegativeValueException(final NegativeValueException e) {
        return new ErrorResponse("Ошибка: передано отрицательное значение!", e.getMessage());
    }
}
