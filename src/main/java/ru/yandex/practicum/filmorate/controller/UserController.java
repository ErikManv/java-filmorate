package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    int userId = 0;
    private void countId() {
        userId++;
    }

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> findAll(){
        return new ArrayList<>(users.values());
    }

    @PostMapping(value="/users")
    public User createUser(@RequestBody User user){
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
        log.info("пользователь {} добавлен", user.getName());
        if(user.getName() == null) {
            user.setName(user.getLogin());
        }
        countId();
        user.setId(userId);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping(value="/users")
    public User updateUser(@RequestBody User user){
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
        if(users.containsKey(user.getId())){
            log.info("пользователь {} обновлен", user.getName());
            users.put(user.getId(), user);
        }else{
            log.error("NOT SUCH id TO UPDATE");
            throw new ValidationException("такого id нет");
        }
        return user;
    }
}
