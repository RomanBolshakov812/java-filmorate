package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.User;

@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert =
                new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        Map<String, String> params = Map.of(
                "name", user.getName(),
                "login", user.getLogin(),
                "email", user.getEmail(),
                "birthday", user.getBirthday().toString());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        Integer userId = user.getId();
        try {
            String sqlQueryExists = "select user_id from users where user_id = ?";
            jdbcTemplate.queryForObject(sqlQueryExists, Integer.class, userId);
        } catch (RuntimeException ex) {
            throw new NullObjectException("Пользователь с id = " + user.getId() + " не найден!");
        }
        String sqlQuery = "update users set name = ?, login = ?, email = ?, "
                    + "birthday = ? where user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                userId);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("select * from users", userRowMapper());
    }

    @Override
    public User getUser(Integer id) {
        User user = new User();
        try {
            user = jdbcTemplate.queryForObject("select * from users where user_id = ?",
                    userRowMapper(), id);
        } catch (RuntimeException e) {
            throw new NullObjectException("Пользователь с id = " + user.getId() + " не найден!");
        }
        return user;
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(rs.getInt("user_id"),
                rs.getString("name"),
                rs.getString("login"),
                rs.getString("email"),
                rs.getDate("birthday").toLocalDate());
    }
}
