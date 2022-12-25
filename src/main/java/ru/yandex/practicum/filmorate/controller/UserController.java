package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping
    public List<User> findAll(){
        return userService.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @GetMapping("/{userId}")
    public User getUsers(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void putFriend(@PathVariable int userId, @PathVariable int newFriendId) {
        userService.putFriend(userId, newFriendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int targetFriendId) {
        userService.deleteFriend(userId, targetFriendId);
    }

    @GetMapping("/{fUserId}/friends/common/{sUserId}")
    public List<User> commonFriends(@PathVariable int fUserId, @PathVariable int sUserId) {
        return userService.commonFriends(fUserId, sUserId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable int userId) {
        return userService.getFriendsList(userId);
    }
}
