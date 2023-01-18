package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User createUser(User user);

    User updateUser(User user);

    User getUser(Integer userId);
    
    List<User> getFriendsList(Integer userId);

    void putFriend(Integer userId, Integer newFriendId);

    void deleteFriend(Integer userId, Integer newFriendId);

    List<User> commonFriends(Integer fUserId, Integer sUserId);
}
