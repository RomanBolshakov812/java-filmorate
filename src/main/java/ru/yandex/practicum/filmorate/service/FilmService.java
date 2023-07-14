package ru.yandex.practicum.filmorate.service;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

public interface FilmService {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilm(Integer id);

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    Integer getRate(Integer filmId);

    List<Film> getPopularFilms(Integer count);

    public List<Mpa> getAllMpa();

    public Mpa getMpa(Integer mpa_id);

    public List<Genre> getAllGenre();

    public Genre getGenre(Integer genre_id);
}
