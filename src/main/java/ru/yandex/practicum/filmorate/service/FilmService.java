package ru.yandex.practicum.filmorate.service;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmService {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilm(Integer id);

    void addLike(Integer userId, Integer filmId);

    void deleteLike(Integer filmId, Integer userId);

    Integer getRate(Integer filmId);

    List<Film> getPopularFilms(Integer count);
}
