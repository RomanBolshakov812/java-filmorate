package ru.yandex.practicum.filmorate.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class InDbUserService implements UserService {

    @Autowired
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    @Autowired
    private final FriendsDao friendsDao;

    @Override
    public User createUser(User user) {
        isValid(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        isValid(user);
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User getUser(Integer id) {
        return userStorage.getUser(id);
    }

    public void addFriend(Integer userId, Integer friendId) {
        User userFirst = userStorage.getUser(userId);
        User userSecond = userStorage.getUser(friendId);
        friendsDao.addFriend(userFirst, userSecond);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        User userFirst = userStorage.getUser(userId);
        User userSecond = userStorage.getUser(friendId);
        friendsDao.deleteFriend(userFirst, userSecond);
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        return friendsDao.getAllFriends(userId);
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        return friendsDao.getCommonFriends(id, otherId);
    }

    private  void isValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() ||
                !user.getEmail().contains("@")) {
            throw new ValidationException("Неверная электронная почта!");
        } else if (user.getLogin() == null || user.getLogin().isBlank() ||
                user.getLogin().contains(" ")) {
            throw new ValidationException("Неверный логин!");
        } else if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неверная дата рождения!");
        }
    }
}
