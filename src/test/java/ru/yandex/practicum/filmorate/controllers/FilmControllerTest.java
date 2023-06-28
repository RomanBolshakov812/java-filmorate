package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private final FilmController filmController = new FilmController();

    Film filmWithEmptyName = new Film(1, "", "description", LocalDate.parse("2000-04-01"), 130);
    Film filmWithDescriptionMore200 = new Film(1, "name", "descriptiodescriptiodescriptiodescriptio" +
            "descriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptio" +
            "descriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptio" +
            "descriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptio" +
            "descriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptio" +
            "descriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptio" +
            "descriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptio" +
            "descriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptio" +
            "descriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptio" +
            "descriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptiodescriptio" +
            "descriptiodescriptiodescriptiodescriptiodescriptiodescription", LocalDate.parse("2000-04-01"), 130);
    Film filmWhereReleaseBeforeLimit = new Film(1, "name", "description", LocalDate.parse("1895-12-27"), 130);
    Film filmWhereZeroDuration = new Film(1, "name", "description", LocalDate.parse("2000-04-01"), 0);
    Film filmWhereNegativeDuration = new Film(1, "name", "description", LocalDate.parse("2000-04-01"), -1);

    @Test
    void shouldBeEmptyName() {
        try {
            filmController.addFilm(filmWithEmptyName);
        } catch (ValidationException e) {
            assertEquals("Название фильма не может быть пустым!", e.getMessage());
        }
    }

    @Test
    void shouldBeDescriptionMore200() {
        try {
            filmController.addFilm(filmWithDescriptionMore200);
        } catch (ValidationException e) {
            assertEquals("Описание должно быть не более 200 символов!", e.getMessage());
        }
    }

    @Test
    void shouldBeReleaseDateBeforeLimit() {
        try {
            filmController.addFilm(filmWhereReleaseBeforeLimit);
        } catch (ValidationException e) {
            assertEquals("Неверная дата релиза!", e.getMessage());
        }
    }

    @Test
    void shouldBeZeroDuration() {
        try {
            filmController.addFilm(filmWhereZeroDuration);
        } catch (ValidationException e) {
            assertEquals("Продолжительность фильма должна быть положительной!", e.getMessage());
        }
    }

    @Test
    void shouldBeNegativeDurations() {
        try {
            filmController.addFilm(filmWhereNegativeDuration);
        } catch (ValidationException e) {
            assertEquals("Продолжительность фильма должна быть положительной!", e.getMessage());
        }
    }
}