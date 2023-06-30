package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generateId = 1;

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        isValid(film);
        film.setId(generateId++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (films.get(film.getId()) == null) {
            throw new NullObjectException("Такого фильма не существует!");
        }
        isValid(film);
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private  void isValid(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым!");
        } else if (film.getDescription() == null || film.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не более 200 символов!");
        } else if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Неверная дата релиза!");
        } else if (film.getDuration() == null || film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }
    }
}
