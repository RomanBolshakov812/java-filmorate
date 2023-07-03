package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);
    User updateUser(User user);
    List<User> getAllUsers();
    User getUser(Integer id);
    void addFriend(Integer userId, Integer friendId);
    void deleteFriend(Integer userId, Integer friendId);
    List<User> getAllFriends(Integer userId);
    List<User> getCommonFriends(Integer id, Integer otherId);
}