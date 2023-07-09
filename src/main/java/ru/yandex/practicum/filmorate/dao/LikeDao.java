package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LikeDao {
    private final JdbcTemplate jdbcTemplate;

    public void addLike(Integer userId, Integer filmId) {
        String sqlQuery = "insert into user_film_like (user_id, film_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from user_film_like where user_id = ? and film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    public Integer getRate(Integer filmId) {
        String sqlQuery = "select count(fl.user_id), fl.film_id from  " +
                "(select user_id, film_id from user_film_like where film_id = ?) as fl group by fl.film_id";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class);
    }
}
