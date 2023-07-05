package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.InMemoryFilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private final FilmController filmController = new FilmController(new InMemoryFilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));

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
        assertThrows(ValidationException.class, () -> filmController.addFilm(filmWithEmptyName));
    }

    @Test
    void shouldBeDescriptionMore200() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(filmWithDescriptionMore200));
    }

    @Test
    void shouldBeReleaseDateBeforeLimit() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(filmWhereReleaseBeforeLimit));
    }

    @Test
    void shouldBeZeroDuration() {
        assertThrows(ValidationException.class, () ->  filmController.addFilm(filmWhereZeroDuration));
    }

    @Test
    void shouldBeNegativeDurations() {
        assertThrows(ValidationException.class, () -> filmController.addFilm(filmWhereNegativeDuration));
    }
}