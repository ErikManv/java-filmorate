package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
public class UserController {

    private HashMap<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public HashMap<Integer, User> findAll(){
        return users;
    }

    @PostMapping(value="/user")
    public User User(@RequestBody User user){
        if(user.getEmail().isEmpty() && !user.getEmail().contains("@")){
            throw new ValidationException("неверный email");
        }
        if(user.getLogin().isEmpty() && !user.getLogin().contains(" ")){
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new ValidationException("неверная дата рождения");
        }
        if(user.getName().isEmpty()){
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping(value="/userUpdate")
    public void updateUser(@RequestBody User user){
        if(users.containsKey(user.getId())){
            users.put(user.getId(), user);
        }else{
            System.out.println("NOT SUCH USER TO UPDATE");
        }
    }

}
