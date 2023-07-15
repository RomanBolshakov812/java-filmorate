package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullObjectException;

@Component
@AllArgsConstructor
public class LikeDao {
    private final JdbcTemplate jdbcTemplate;

    public void addLike(Integer filmId, Integer userId) {
        String sqlQuery = "insert into user_film_like (film_id, user_id) values (?, ?); "
                + "update films set rate = rate + 1 where film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId, filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from user_film_like where user_id = ? and film_id = ?";
        String sqlQueryExists = "select exists(select f.film_id, ufl.user_id from films f "
                + "left join user_film_like ufl on f.film_id = ufl.film_id "
                + "where f.film_id = ? and ufl.user_id = ?)";
        if (Boolean.FALSE.equals(jdbcTemplate.queryForObject(sqlQueryExists,
                Boolean.class, filmId, userId))) {
            throw new NullObjectException("Фильм с id " + filmId + " "
                    + "или пользователь с id " + userId + " не найден!");
        }
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    public Integer getRate(Integer filmId) {
        String sqlQuery = "select count(fl.user_id), fl.film_id from  "
                + "(select user_id, film_id from user_film_like where film_id = ?) as fl "
                + "group by fl.film_id";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class);
    }
}
