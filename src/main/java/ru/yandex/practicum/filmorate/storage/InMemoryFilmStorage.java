package ru.yandex.practicum.filmorate.storage;

import java.util.*;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Film;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int generateId = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(generateId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (getFilm(film.getId()) != null) {
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Integer id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NullObjectException("Фильм с id = " + id + "  не найден");
        }
        return film;
    }
}
