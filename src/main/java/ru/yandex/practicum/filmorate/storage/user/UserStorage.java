package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User createUser(User user);

    boolean containsUser(int userId);

    User updateUser(User user);

    User getUser(int userId);

    List<User> getFriendsList(int userId);

    void putFriend(int userId, int newFriendId);

    void deleteFriend(int userId, int newFriendId);

    List<User> commonFriends(int fUserId, int sUserId);
}
