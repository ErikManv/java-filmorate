package ru.yandex.practicum.filmorate.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        if(filmStorage.containsFilm(filmId)){
            return filmStorage.getFilm(filmId);
        }
        else{
            throw new NullPointerException("такого id нет");
        }
    }

    public Film updateFilm(Film film){
        validator(film);
        if(filmStorage.containsFilm(film.getId())){
            log.info("фильм {} обновлен", film.getName());
            filmStorage.updateFilm(film);
        }
        else{
            log.error("NOT SUCH FILM TO UPDATE");
            throw new NullPointerException("такого id нет");
        }
        return film;
    }

    public List<Film> topFilms(Integer count){
        return filmStorage.topFilms(count);
    }


    public void putLike(Integer filmId, Integer userId){
        idValidator(filmId);
        if(userStorage.containsUser(userId)) {
            filmStorage.putLike(filmId, userId);
        } else {
            throw new NullPointerException("Пользователя с таким id нет");
        }
    }


    public void deleteLike(Integer filmId, Integer userId) {
        idValidator(filmId);
        if(userStorage.containsUser(userId)) {
            filmStorage.deleteLike(filmId, userId);
        } else {
            throw new NullPointerException("Пользователя с таким id нет");
        }
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

}
