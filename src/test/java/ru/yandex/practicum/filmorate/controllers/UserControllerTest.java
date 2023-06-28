package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final UserController userController = new UserController();

    User userWithEmptyEmail = new User(1, "", "login", "name", LocalDate.parse("1965-12-10"));
    User userWithIncorrectEmail = new User(1, "mail.ru", "login", "name", LocalDate.parse("1965-12-10"));
    User userWithEmptyLogin = new User(1, "user@mail.ru", "", "name", LocalDate.parse("1965-12-10"));
    User userWithIncorrectLogin = new User(1, "user@mail.ru", "login login", "name", LocalDate.parse("1965-12-10"));
    User userWithEmptyName = new User(1, "user@mail.ru", "login", "", LocalDate.parse("1965-12-10"));
    User userWithIncorrectBirthday = new User(1, "user@mail.ru", "login", "name", LocalDate.now().plusDays(1));

    @Test
    void shouldBeEmptyEmail() {
        try {
            userController.createUser(userWithEmptyEmail);
        } catch (ValidationException e) {
            assertEquals("Неверная электронная почта!", e.getMessage());
        }
    }

    @Test
    void shouldBeIncorrectEmail() {
        try {
            userController.createUser(userWithIncorrectEmail);
        } catch (ValidationException e) {
            assertEquals("Неверная электронная почта!", e.getMessage());
        }
    }

    @Test
    void shouldBeEmptyLogin() {
        try {
            userController.createUser(userWithEmptyLogin);
        } catch (ValidationException e) {
            assertEquals("Неверный логин!", e.getMessage());
        }
    }

    @Test
    void shouldBeIncorrectLogin() {
        try {
            userController.createUser(userWithIncorrectLogin);
        } catch (ValidationException e) {
            assertEquals("Неверный логин!", e.getMessage());
        }
    }

    @Test
    void shouldBeEmptyName() {
        userController.createUser(userWithEmptyName);
        assertEquals(userWithEmptyName.getName(), userWithEmptyName.getLogin());
    }

    @Test
    void shouldBeIncorrectBirthday() {
        try {
            userController.createUser(userWithIncorrectBirthday);
        } catch (ValidationException e) {
            assertEquals("Неверная дата рождения!", e.getMessage());
        }
    }
}