package ru.yandex.practicum.filmorate.dao;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Genre;

@Component
@AllArgsConstructor
public class GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public List<Genre> getAllGenre() {
        String sqlQuery = "select * from genres order by genre_id";
        return jdbcTemplate.queryForObject(sqlQuery, allGenreRowMapper());
    }

    public Genre getGenre(Integer genreId) {
        Genre genre = new Genre();
        try {
            genre = jdbcTemplate.queryForObject("select * from genres where genre_id = ?",
                    genreRowMapper(), genreId);
        } catch (RuntimeException e) {
            throw new NullObjectException("Жанр с id = " + genre.getId() + " не найден!");
        }
        return genre;
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("genre_name"));
    }

    private RowMapper<List<Genre>> allGenreRowMapper() {
        return (rs, rowNum) -> {
            List<Genre> allGenre = new ArrayList<>();
            do {
                Genre genre = new Genre();
                genre.setId(rs.getInt("genre_id"));
                genre.setName(rs.getString("genre_name"));
                allGenre.add(genre);
            } while (rs.next());
            return allGenre;
        };
    }
}
