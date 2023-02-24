package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public List<User> findAll(){
        return userStorage.findAll();
    }

    public User createUser(User user){
        if(user.getName().isEmpty() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        validator(user);
        log.info("пользователь {} добавлен", user.getName());
        userStorage.createUser(user);
        return user;
    }

    public User getUser(Integer userId) {
        idValidator(userId);
        containsUser(userId);
        return userStorage.getUser(userId);
    }

    public List<User> getFriendsList(Integer userId) {
        idValidator(userId);
        containsUser(userId);
        return userStorage.getFriendsList(userId);
    }

    public ResponseEntity<User> updateUser(User user){
        try {
            validator(user);
            containsUser(user.getId());
            log.info("пользователь {} обновлен", user.getName());
            return new ResponseEntity<>(userStorage.updateUser(user), HttpStatus.OK);
        }
        catch (NotFoundException exception) {
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
    }

    public void putFriend(Integer userId, Integer newFriendId) {
        idValidator(userId);
        idValidator(newFriendId);
        containsUser(userId);
        containsUser(newFriendId);
        userStorage.putFriend(userId, newFriendId);
    }

    public void deleteFriend(Integer userId, Integer targetFriendId) {
        idValidator(userId);
        idValidator(targetFriendId);
        containsUser(userId);
        containsUser(targetFriendId);
        userStorage.deleteFriend(userId, targetFriendId);
    }

    public List<User> commonFriends(Integer fUserId, Integer sUserId) {
        idValidator(fUserId);
        idValidator(sUserId);
        containsUser(fUserId);
        containsUser(sUserId);
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

    private void idValidator(Integer id) {
        if(id < 1) {
            log.error("неверный id");
            throw new NullPointerException("id не может быть пустым или отрицательным");
        }
    }

    public void containsUser(Integer userId) {
        List<Integer> ids = new ArrayList<>();
        for(User user1: userStorage.findAll()) {
            ids.add(user1.getId());
        }
        if(!ids.contains(userId)) {
            throw new NotFoundException("не найдено");
        }
    }
}
