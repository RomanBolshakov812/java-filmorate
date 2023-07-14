package ru.yandex.practicum.filmorate.storage;

import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

@Component("filmDbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        if (film.getRate() == null) {
            film.setRate(0);
        }
        SimpleJdbcInsert simpleJdbcInsert =
                new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        Map<String, String> params = Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate().toString(),
                "duration", film.getDuration().toString(),
                "rate", film.getRate().toString(),
                "mpa_id", film.getMpa().getId().toString());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id.intValue());
        film.setGenres(updateGenres(film));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Integer filmId = film.getId();
        try {
            String sqlQueryExists = "select film_id from films where film_id = ?";
            jdbcTemplate.queryForObject(sqlQueryExists, Integer.class, filmId);
        } catch (RuntimeException ex) {
            throw new NullObjectException("Фильм с id = " + filmId + " не найден!");
        }
        String sqlQueryUpdateFilm = "update films set name = ?, description = ?, release_date= ?, "
                + "duration = ?, rate = ?, mpa_id = ? where film_id = ?";
        String sqlQueryUpdateGenreInFilm = "insert into film_genres (film_id, genre_id) "
                + "values (?, ?)";
        jdbcTemplate.update(sqlQueryUpdateFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                filmId);
        film.setGenres(updateGenres(film));
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "select f.*, m.mpa_name, g.genre_id, g.genre_name from films f "
                + "left join mpa m on f.mpa_id = m.mpa_id "
                + "left join film_genres fg on f.film_id = fg.film_id "
                + "left join genres g on fg.genre_id = g.genre_id order by f.film_id";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, allFilmRowMapper());
        } catch (RuntimeException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Film getFilm(Integer id) {
        String sqlQuery = "select f.*, m.mpa_name, g.genre_id, genre_name from films f "
                + "left join mpa m on f.mpa_id = m.mpa_id "
                + "left join film_genres fg on f.film_id = fg.film_id "
                + "left join genres g on fg.genre_id = g.genre_id where f.film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, filmRowMapper(), id);
        } catch (RuntimeException e) {
            throw new NullObjectException("Фильм с id = " + id + " не найден!");
        }
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getInt("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setRate(rs.getInt("rate"));
            Mpa mpa = new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"));
            film.setMpa(mpa);
            List<Genre> genresList = new ArrayList<>();
            do {
                Genre genre = new Genre(rs.getInt("genre_id"),
                        rs.getString("genre_name"));
                if (genre.getName() != null) {
                    genresList.add(genre);
                }
            } while (rs.next());
            film.setGenres(genresList);
            return film;
        };
    }

    private RowMapper<List<Film>> allFilmRowMapper() {
        return (rs, rowNum) -> {
            List<Film> allFilms = new ArrayList<>();
            boolean isExistingNextLine = true;
            boolean isExistingNextGenre;
            do {
                Film film = new Film();
                film.setId(rs.getInt("film_id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                film.setRate(rs.getInt("rate"));
                Mpa mpa = new Mpa(rs.getInt("mpa_id"),
                        rs.getString("mpa_name"));
                film.setMpa(mpa);
                do {
                    String genreName = rs.getString("genre_name");
                    if (genreName != null) {
                        Genre genre = new Genre(rs.getInt("genre_id"), genreName);
                        film.getGenres().add(genre);
                    }
                    if (rs.next()) {
                        if (rs.getInt("film_id") == film.getId()) {
                            isExistingNextGenre = true;
                        } else {
                            isExistingNextGenre = false;
                            allFilms.add(film);
                        }
                    } else {
                        isExistingNextLine = false;
                        isExistingNextGenre = false;
                        allFilms.add(film);
                    }
                } while (isExistingNextGenre);
            } while (isExistingNextLine);
            return allFilms;
        };
    }

    private List<Genre> updateGenres(Film film) {
        String sqlQueryDelete = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(sqlQueryDelete, film.getId());
        List<Genre> genres = film.getGenres();
        Set<Genre> set = new HashSet<>();
        List<Genre> newGenres = new ArrayList<>();
        genres.forEach(n -> {
            if (set.add(n)) {
                newGenres.add(n);
            }
        });
        int filmId = film.getId();
        String sqlQuery;
        StringBuilder sb =
                new StringBuilder("insert into film_genres (film_id, genre_id) values (");
        if (!newGenres.isEmpty()) {
            if (newGenres.size() == 1) {
                sqlQuery = "insert into film_genres (film_id, genre_id) "
                        + "values (" + filmId + ", " +  newGenres.get(0).getId() + ")";
            } else {
                sb.append(filmId).append(", ").append(newGenres.get(0).getId()).append(")");
                for (int i = 1; i < newGenres.size(); i++) {
                    int genreId = newGenres.get(i).getId();
                    sb.append(", (").append(filmId).append(", ").append(genreId).append(")");
                }
                sqlQuery = sb.toString();
            }

            jdbcTemplate.update(sqlQuery);
        }
        return newGenres;
    }
}
