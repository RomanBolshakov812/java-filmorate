package ru.yandex.practicum.filmorate.dao;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Mpa;

@Component
@AllArgsConstructor
public class MpaDao {

    JdbcTemplate jdbcTemplate;

    public List<Mpa> getAllMpa() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.queryForObject(sqlQuery, allMpaRowMapper());
    }

    public Mpa getMpa(Integer mpaId) {
        Mpa mpa = new Mpa();
        try {
            mpa = jdbcTemplate.queryForObject("select * from mpa where mpa_id = ?",
                    mpaRowMapper(), mpaId);
        } catch (RuntimeException e) {
            throw new NullObjectException("Mpa с id = " + mpa.getId() + " не найден!");
        }
        return mpa;
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> new Mpa(rs.getInt("mpa_id"),
                rs.getString("mpa_name"));
    }

    private RowMapper<List<Mpa>> allMpaRowMapper() {
        return (rs, rowNum) -> {
            List<Mpa> allMpa = new ArrayList<>();
            do {
                Mpa mpa = new Mpa();
                mpa.setId(rs.getInt("mpa_id"));
                mpa.setName(rs.getString("mpa_name"));
                allMpa.add(mpa);
            } while (rs.next());
            return allMpa;
        };
    }
}
