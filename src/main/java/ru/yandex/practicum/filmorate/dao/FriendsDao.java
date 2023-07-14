package ru.yandex.practicum.filmorate.dao;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
@RequiredArgsConstructor
public class FriendsDao {

    private final JdbcTemplate jdbcTemplate;

    public void addFriend(User requestingUser, User targetUser) {
        Integer requestingUserId = requestingUser.getId();
        Integer targetUserId = targetUser.getId();
        String sqlQuery = "select user_id from user_friends where user_id = ? and friend_id = ?";
        Integer possibleFriendId;
        try {
            possibleFriendId = jdbcTemplate.queryForObject(sqlQuery, Integer.class,
                    targetUserId, requestingUserId);
        } catch (RuntimeException e) {
            possibleFriendId = null;
        }
        if (Objects.equals(possibleFriendId, targetUserId)) {
            String sqlQueryUpdate = "update user_friends set confirmed = true where "
                    + "user_id = ? and friend_id = ?";
            String sqlQueryInsert = "insert into user_friends (user_id, friend_id, confirmed) "
                    + "values (?, ?, true)";
            jdbcTemplate.update(sqlQueryUpdate,
                    targetUserId,
                    requestingUserId);
            jdbcTemplate.update(sqlQueryInsert,
                    requestingUserId,
                    targetUserId);
        } else {
            String sqlQueryInsert = "insert into user_friends (user_id, friend_id, confirmed) "
                    + "values (?, ?, false)";
            jdbcTemplate.update(sqlQueryInsert,
                    requestingUserId,
                    targetUserId);
        }
    }

    public void deleteFriend(User userFirst, User userSecond) {
        Integer userFirstId = userFirst.getId();
        Integer userSecondId = userSecond.getId();
        String sqlQuery = "delete from user_friends where (user_id = ? and friend_id = ?) "
                + "or (user_id = ? and friend_id = ?);";
        jdbcTemplate.update(sqlQuery, userFirstId, userSecondId, userSecondId, userFirstId);
    }

    public List<User> getAllFriends(Integer userId) {
        String sqlQuery = "select u.user_id, u.name, u.login, u.email, u.birthday "
                + "from user_friends uf left join users u "
                + "on u.user_id = uf.friend_id where uf.user_id = ?";
        return jdbcTemplate.query(sqlQuery, userRowMapper(), userId);
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        String sqlQuery = "select u.user_id, u.name, u.login, u.email, u.birthday from users u "
                + "join (select uf.friend_id, count(*) "
                + "from (select user_id id, friend_id, confirmed from user_friends  "
                + "where user_id = ? or user_id = ?) as uf "
                + "group by uf.friend_id having count(*) > 1) as of on of.friend_id = u.user_id";
        return jdbcTemplate.query(sqlQuery, userRowMapper(), id, otherId);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(rs.getInt("user_id"),
                rs.getString("name"),
                rs.getString("login"),
                rs.getString("email"),
                rs.getDate("birthday").toLocalDate());
    }
}
