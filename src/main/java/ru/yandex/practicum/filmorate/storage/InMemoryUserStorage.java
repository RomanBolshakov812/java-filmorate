package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;

    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User createUser(User user) {
        user.setId(generateId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        isNotNullUser(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Integer id) {
        isNotNullUser(id);
        return users.get(id);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        isNotNullUser(userId);
        isNotNullUser(friendId);
        users.get(userId).getFriends().add(friendId);
        users.get(friendId).getFriends().add(userId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        isNotNullUser(userId);
        isNotNullUser(friendId);
        users.get(userId).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(userId);
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        isNotNullUser(userId);
        if (users.get(userId).getFriends() == null || users.get(userId).getFriends().isEmpty()) {
            throw new NullObjectException("У пользователя нет друзей!");
        }
        List<User> friendsList = new ArrayList<>();
        for (Integer id : users.get(userId).getFriends()) {
            friendsList.add(users.get(id));
        }
        return friendsList;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        isNotNullUser(id);
        isNotNullUser(otherId);
        Set<Integer> firstUserFriends = users.get(id).getFriends();
        Set<Integer> secondUserFriends = users.get(otherId).getFriends();
        List<User> commonFriends = new ArrayList<>();
        for (Integer currentId : firstUserFriends) {
            if (secondUserFriends.contains(currentId)) {
                commonFriends.add(users.get(currentId));
            }
        }
        return commonFriends;
    }

    private void isNotNullUser(Integer id) {
        if (id < 0 || users.get(id) == null) {
            throw new NullObjectException("Пользователя с id = " + id + " не существует!");
        }
    }
}
