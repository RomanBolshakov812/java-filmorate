package ru.yandex.practicum.filmorate.service;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmService {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilm(Integer id);

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    List<Film> getPopularFilms(Integer count);
}
