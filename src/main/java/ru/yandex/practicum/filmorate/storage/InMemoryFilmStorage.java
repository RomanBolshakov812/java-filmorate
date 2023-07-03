package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

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
        isNotNullFilm(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Integer id) {
        isNotNullFilm(id);
        return films.get(id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        isNotNullFilm(filmId);
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        isNotNullFilm(filmId);
        if (userId == null || userId < 0) {
            throw new NullObjectException("Пользователя с id = " + userId + " не существует!");
        }
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {

        List<Film> sortedFilms = new ArrayList<>(films.values());

        Comparator<Film> comparator = new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return  o2.getLikes().size() - o1.getLikes().size();
            }
        };
        sortedFilms.sort(comparator);

        if (sortedFilms.size() > count) {
            sortedFilms = sortedFilms.subList(0, count);
        }

        return sortedFilms;
    }

    private void isNotNullFilm(Integer id) {
        if (id < 0 || films.get(id) == null) {
            throw new NullObjectException("Фильма с id = " + id + " не существует!");
        }
    }
}
