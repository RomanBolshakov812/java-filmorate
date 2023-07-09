package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("filmDbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    @Autowired
    private final LikeDao likeDao;
    private JdbcTemplate jdbcTemplate;
    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        Map<String, String> params = Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate().toString(),
                "duration", film.getDuration().toString(),
                "rate", film.getRate().toString(),
                "mpa", film.getMpa().toString());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id.intValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update films set name = ?, description = ?, release_date= ?, duration = ?, " +
                "rate = ?, mpa = ?, genres = ? where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa(),
                film.getGenres(),
                film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query("select * from films", filmRowMapper());
    }

    @Override
    public Film getFilm(Integer id) {
        String sqlQuery = "select f.film_id, f.name, f.description, f.release_date, f.duration, m.mpa_id , g.genre_id" +
                "from films f join mpa m on f.mpa = m.mpa_id left join film_genres fg on f.film_id = fg.film_id " +
                "join genres g on fg.genre_id = g.genre_id where f.film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, filmRowMapper(), id);
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getInt("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setRate(likeDao.getRate(rs.getInt("film_id")));
            film.setMpa(rs.getInt("mpa"));
            List<Integer> genresIdList = new ArrayList<>();
            do {
                Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("name"));
                genresIdList.add(genre.getId());
            } while (rs.next());
            film.setGenres(genresIdList);
            return film;
        };
    }
}
