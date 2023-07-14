package ru.yandex.practicum.filmorate.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {

    @Autowired
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Autowired
    private final LikeDao likeDao;
    @Autowired
    private final MpaDao mpaDao;
    @Autowired
    private final GenreDao genreDao;

    @Override
    public Film addFilm(Film film) {
        isValid(film);
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        isValid(film);
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        likeDao.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        likeDao.deleteLike(filmId, userId);
    }

    @Override
    public Integer getRate(Integer filmId) {
        return likeDao.getRate(filmId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        List<Film> sortedFilms = filmStorage.getAllFilms();
        Comparator<Film> comparator = (o1, o2) -> o2.getRate() - o1.getRate();
        sortedFilms.sort(comparator);
        if (sortedFilms.size() > count) {
            sortedFilms = sortedFilms.subList(0, count);
        }
        return sortedFilms;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    @Override
    public Mpa getMpa(Integer mpaId) {
        return mpaDao.getMpa(mpaId);
    }

    @Override
    public List<Genre> getAllGenre() {
        return genreDao.getAllGenre();
    }

    @Override
    public Genre getGenre(Integer genreId) {
        return genreDao.getGenre(genreId);
    }

    private  void isValid(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым!");
        } else if (film.getDescription() == null || film.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не более 200 символов!");
        } else if (film.getReleaseDate() == null || film.getReleaseDate()
                .isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Неверная дата релиза!");
        } else if (film.getDuration() == null || film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        } else if (film.getMpa() == null) {
            throw new ValidationException("Необходимо указать рейтинг MPA!");
        }
    }
}
