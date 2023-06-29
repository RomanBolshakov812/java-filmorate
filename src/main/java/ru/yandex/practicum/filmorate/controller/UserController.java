package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;

    @PostMapping
    public User createUser(@RequestBody User user) {
        isValid(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(generateId++);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (users.get(user.getId()) == null) {
            throw new NullObjectException("Такого пользователя не существует!");
        }
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private  void isValid(User user) {
        if (user.getEmail().isBlank() | !user.getEmail().contains("@")) {
            throw new ValidationException("Неверная электронная почта!");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Неверный логин!");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неверная дата рождения!");
        }
    }
}
