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

    public User getUser(Integer userId) {
        idValidator(userId);
        if(userStorage.containsUser(userId)){
            return userStorage.getUser(userId);
        }else{
            log.error("такого id не существует");
            throw new NullPointerException("такого id нет");
        }
    }

    public List<User> getFriendsList(Integer userId) {
        idValidator(userId);
        if(userStorage.containsUser(userId)){
            return userStorage.getFriendsList(userId);
        }else{
            log.error("такого id не существует");
            throw new NullPointerException("такого id нет");
        }
    }

    public User updateUser(User user){
        validator(user);
        if(userStorage.containsUser(user.getId())){
            log.info("пользователь {} обновлен", user.getName());
            userStorage.updateUser(user);
        }else{
            log.error("такого id не существует");
            throw new NullPointerException("такого id нет");
        }
        return user;
    }

    public void putFriend(Integer userId, Integer newFriendId) {
        idValidator(userId);
        idValidator(newFriendId);
        if(userStorage.containsUser(userId) && userStorage.containsUser(newFriendId)) {
            userStorage.putFriend(userId, newFriendId);
        }else {
            throw new NullPointerException("Пользователя с таким id нет");
        }
    }

    public void deleteFriend(Integer userId, Integer targetFriendId) {
        idValidator(userId);
        idValidator(targetFriendId);
        if(userStorage.containsUser(userId) && userStorage.containsUser(targetFriendId)) {
            userStorage.deleteFriend(userId, targetFriendId);
        }else {
            throw new NullPointerException("Пользователя с таким id нет");
        }
    }

    public List<User> commonFriends(Integer fUserId, Integer sUserId) {
        idValidator(fUserId);
        idValidator(sUserId);
        if(userStorage.containsUser(fUserId) && userStorage.containsUser(sUserId)) {
            return userStorage.commonFriends(fUserId, sUserId);
        }else {
            throw new NullPointerException("Пользователя с таким id нет");
        }
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

    private void idValidator(Integer id) {
        if(id < 1) {
            log.error("неверный id");
            throw new NullPointerException("id не может быть пустым или отрицательным");
        }
    }
}
