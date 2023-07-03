package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    List<Film> getAllFilms();
    Film getFilm(Integer id);
    void addLike(Integer filmId, Integer userId);
    void deleteLike(Integer filmId, Integer userId);
    List<Film> getPopularFilms(Integer count);
}
