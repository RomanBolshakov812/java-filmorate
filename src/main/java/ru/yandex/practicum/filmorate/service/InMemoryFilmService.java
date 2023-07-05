package ru.yandex.practicum.filmorate.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
@AllArgsConstructor
public class InMemoryFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

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
        Film film = filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
        film.getLikes().add(userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilm(filmId);
        userStorage.getUser(userId);
        film.getLikes().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        List<Film> sortedFilms = filmStorage.getAllFilms();
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
