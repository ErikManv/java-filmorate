package ru.yandex.practicum.filmorate.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    private FilmStorage filmStorage;
    private UserService userService;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage")FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    public List<Film> findAll(){
        return filmStorage.findAll();
    }

    public Film addFilm(Film film){
        validator(film);
        log.info("фильм {} добавлен", film.getName());
        filmStorage.addFilm(film);
        return film;
    }
    
    public Film getFilm(Integer filmId) {
        idValidator(filmId);
        containsFilm(filmId);
        return filmStorage.getFilm(filmId);
    }

    public ResponseEntity<Film> updateFilm(Film film){
        try{
            validator(film);
            containsFilm(film.getId());
            log.info("фильм {} обновлен", film.getName());
            return new ResponseEntity<>(filmStorage.updateFilm(film), HttpStatus.OK);
        }catch(NotFoundException exception) {
            return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
        }
    }

    public List<Film> topFilms(Integer count){
        return filmStorage.topFilms(count);
    }


    public void putLike(Integer filmId, Integer userId){
        idValidator(filmId);
        containsFilm(filmId);
        userService.containsUser(userId);
        filmStorage.putLike(filmId, userId);
    }


    public void deleteLike(Integer filmId, Integer userId) {
        idValidator(filmId);
        containsFilm(filmId);
        userService.containsUser(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    private void validator(Film film){
        if(film.getName().isBlank()) {
            log.error("пустое имя");
            throw new ValidationException("Имя не может быть пустым");
        }
        if(film.getDescription().length() > 200) {
            log.error("слишком большое описание");
            throw new ValidationException("Длинна описания более 200 символов");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("неверная дата");
            throw new ValidationException("Не раньше 1895-12-28");
        }
        if(film.getDuration() < 0) {
            log.error("отрицательное число");
            throw new ValidationException("Длительность должна быть положительной");
        }
    }

    private void idValidator(Integer id) {
        if(id < 1) {
            log.error("неверный id");
            throw new NullPointerException("id не может быть пустым или отрицательным");
        }
    }

    private void containsFilm(Integer filmId) {
        List<Integer> ids = new ArrayList<>();
        for(Film film1: filmStorage.findAll()) {
            ids.add(film1.getId());
        }
        if(!ids.contains(filmId)) {
            throw new NotFoundException("не найдено");
        }
    }
}
