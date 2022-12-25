package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public List<User> findAll(){
        return userStorage.findAll();
    }

    public User createUser(User user){
        validator(user);
        log.info("пользователь {} добавлен", user.getName());
        userStorage.createUser(user);
        return user;
    }

    public User getUser(int userId) {
        return userStorage.getUser(userId);
    }

    public List<User> getFriendsList(int userId) {
        return userStorage.getFriendsList(userId);
    }

    public User updateUser(User user){
        validator(user);
        if(userStorage.containsUser(user.getId())){
            log.info("пользователь {} обновлен", user.getName());
            userStorage.updateUser(user);
        }else{
            log.error("такого id не существует");
            throw new ValidationException("такого id нет");
        }
        return user;
    }

    public void putFriend(int userId, int newFriendId) {
        userStorage.putFriend(userId, newFriendId);
    }

    public void deleteFriend(int userId, int targetFriendId) {
        userStorage.deleteFriend(userId, targetFriendId);
    }

    public List<User> commonFriends(int fUserId, int sUserId) {
        return userStorage.commonFriends(fUserId, sUserId);
    }

    private void validator(User user) {
        if(user.getEmail().isEmpty() || !user.getEmail().contains("@")){
            log.error("неверный email");
            throw new ValidationException("неверный email");
        }
        if(user.getLogin().isEmpty() || user.getLogin().contains(" ")){
            log.error("неверный логин");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            log.error("неверная дата рождения");
            throw new ValidationException("неверная дата рождения");
        }
    }
}
