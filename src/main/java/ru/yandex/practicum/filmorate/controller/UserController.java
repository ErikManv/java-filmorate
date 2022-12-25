package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void putFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer newFriendId) {
        userService.putFriend(userId, newFriendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer targetFriendId) {
        userService.deleteFriend(userId, targetFriendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable("id") Integer fUserId, @PathVariable("otherId") Integer sUserId) {
        return userService.commonFriends(fUserId, sUserId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Integer userId) {
        return userService.getFriendsList(userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
// отлавливаем исключение IllegalArgumentException
    public ErrorResponse handleNullPointer(final NullPointerException e) {
        // возвращаем сообщение об ошибке
        return new ErrorResponse(
                "Объект не найден", e.getMessage()
        );
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
// отлавливаем исключение IllegalArgumentException
    public ErrorResponse handleValidation(final ValidationException e) {
        // возвращаем сообщение об ошибке
        return new ErrorResponse(
                "Ошибка валидации", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
// отлавливаем исключение IllegalArgumentException
    public ErrorResponse handleThrowable(final Throwable e) {
        // возвращаем сообщение об ошибке
        return new ErrorResponse(
                "Произошла непредвиденная ошибка", e.getMessage()
        );
    }
}
