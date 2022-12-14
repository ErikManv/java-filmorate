package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
public class UserController {

    int userId = 0;
    private void countId() {
        userId++;
    }

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public HashMap<Integer, User> findAll(){
        return users;
    }

    @PostMapping(value="/user")
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
        if(user.getName().isEmpty()){
            user.setName(user.getLogin());
        }
        log.info("пользователь {} добавлен", user.getName());
        countId();
        user.setId(userId);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping(value="/userUpdate")
    public void updateUser(@RequestBody User user){
        if(users.containsKey(user.getId())){
            log.info("пользователь {} обновлен", user.getName());
            users.put(user.getId(), user);
        }else{
            System.out.println("NOT SUCH USER TO UPDATE");
        }
    }

}