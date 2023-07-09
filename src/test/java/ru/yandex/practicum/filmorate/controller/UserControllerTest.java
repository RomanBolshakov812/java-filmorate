package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.InMemoryUserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final UserController userController = new UserController(new InMemoryUserService(new UserDbStorage()));

    User userWithEmptyEmail = new User(1, "", "login", "name", LocalDate.parse("1965-12-10"));
    User userWithIncorrectEmail = new User(1, "mail.ru", "login", "name", LocalDate.parse("1965-12-10"));
    User userWithEmptyLogin = new User(1, "user@mail.ru", "", "name", LocalDate.parse("1965-12-10"));
    User userWithIncorrectLogin = new User(1, "user@mail.ru", "login login", "name", LocalDate.parse("1965-12-10"));
    User userWithEmptyName = new User(1, "user@mail.ru", "login", "", LocalDate.parse("1965-12-10"));
    User userWithIncorrectBirthday = new User(1, "user@mail.ru", "login", "name", LocalDate.now().plusDays(1));

    @Test
    void shouldBeEmptyEmail() {
        assertThrows(ValidationException.class, () -> userController.createUser(userWithEmptyEmail));
    }

    @Test
    void shouldBeIncorrectEmail() {
        assertThrows(ValidationException.class, () -> userController.createUser(userWithIncorrectEmail));
    }

    @Test
    void shouldBeEmptyLogin() {
        assertThrows(ValidationException.class, () -> userController.createUser(userWithEmptyLogin));
    }

    @Test
    void shouldBeIncorrectLogin() {
        assertThrows(ValidationException.class, () -> userController.createUser(userWithIncorrectLogin));
    }

    @Test
    void shouldBeEmptyName() {
        userController.createUser(userWithEmptyName);
        assertEquals(userWithEmptyName.getName(), userWithEmptyName.getLogin());
    }

    @Test
    void shouldBeIncorrectBirthday() {
        assertThrows(ValidationException.class, () -> userController.createUser(userWithIncorrectBirthday));
    }
}