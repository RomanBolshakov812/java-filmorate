package ru.yandex.practicum.filmorate.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
@AllArgsConstructor
public class InMemoryUserService implements UserService {

    private final UserStorage userStorage;

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
        userFirst.getFriends().add(friendId);
        userSecond.getFriends().add(userId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        User userFirst = userStorage.getUser(userId);
        User userSecond = userStorage.getUser(friendId);
        userFirst.getFriends().remove(friendId);
        userSecond.getFriends().remove(userId);
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        Set<Integer> friendIdList = userStorage.getUser(userId).getFriends();
        if (friendIdList == null || friendIdList.isEmpty()) {
            throw new NullObjectException("У пользователя нет друзей!");
        }
        List<User> friendsList = new ArrayList<>();
        for (Integer id : friendIdList) {
            friendsList.add(userStorage.getUser(id));
        }
        return friendsList;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        Set<Integer> firstUserFriends = userStorage.getUser(id).getFriends();
        Set<Integer> secondUserFriends = userStorage.getUser(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();
        for (Integer currentId : firstUserFriends) {
            if (secondUserFriends.contains(currentId)) {
                commonFriends.add(userStorage.getUser(currentId));
            }
        }
        return commonFriends;
    }

    private  void isValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Неверная электронная почта!");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Неверный логин!");
        } else if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неверная дата рождения!");
        }
    }
}
