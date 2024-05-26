package ru.yandex.practicum.filmorate.storage;

import java.util.*;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.User;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;

    @Override
    public User createUser(User user) {
        user.setId(generateId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (getUser(user.getId()) != null) {
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Integer id) {
        User user = users.get(id);
        if (user == null) {
            throw new NullObjectException("Пользователь с id = " + id + "  не найден");
        }
        return user;
    }
}
