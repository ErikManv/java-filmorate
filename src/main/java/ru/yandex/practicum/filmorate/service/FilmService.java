package ru.yandex.practicum.filmorate.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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

    public Film updateFilm(Film film){
        validator(film);
        if(filmStorage.containsFilm(film.getId())){
            log.info("фильм {} обновлен", film.getName());
            filmStorage.updateFilm(film);
        }else{
            log.error("NOT SUCH FILM TO UPDATE");
            throw new ValidationException("такого id нет");
        }
        return film;
    }

    public List<Film> topFilms(int count){
        return filmStorage.topFilms(count);
    }


    public void putLike(int filmId, int userId){
        filmStorage.putLike(filmId, userId);
    }


    public void deleteLike(int filmId, int userId) {
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

}
