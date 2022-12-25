package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping
    public List<Film> findAll(){
        return filmService.findAll();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film){
        return filmService.addFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Integer filmId) {
        return filmService.getFilm(filmId);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film){
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable("id") Integer filmId, @PathVariable Integer userId) {
        filmService.putLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId, @PathVariable Integer userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> topFilms (@RequestParam(defaultValue = "10") Integer count){
        return filmService.topFilms(count);
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
