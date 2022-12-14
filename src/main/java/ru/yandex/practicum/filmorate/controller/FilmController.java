package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {
    int filmId = 0;
    private void countId() {
        filmId++;
    }

    private Map <Integer, Film> films = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);


    @GetMapping("/films")
    public Map<Integer, Film> findAll(){
        return films;
    }

    @PostMapping(value="/film")
    public Film addFilm(@RequestBody Film film){
        if(film.getName().isBlank()) {
            log.error("пустое имя");
            throw new ValidationException("Имя не может быть пустым");
        }
        if(film.getDescription().length() > 200) {
            log.error("слишком большое описание");
            throw new ValidationException("Длинна описания более 200 символов");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1968, 12, 28))) {
            log.error("неверная дата");
            throw new ValidationException("Не раньше 1968-12-28");
        }
        if(film.getDuration() < 0) {
            log.error("отрицательное число");
            throw new ValidationException("Длительность должна быть положительной");
        }

        log.info("фильм {} добавлен", film.getName());
        countId();
        film.setId(filmId);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value="/filmUpdate")
    public Film updateFilm(@RequestBody Film film){
        if(film.getName().isBlank()) {
            log.error("пустое имя");
            throw new ValidationException("Имя не может быть пустым");
        }
        if(film.getDescription().length() > 200) {
            log.error("слишком большое описание");
            throw new ValidationException("Длинна описания более 200 символов");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1968, 12, 28))) {
            log.error("неверная дата");
            throw new ValidationException("Не раньше 1968-12-28");
        }
        if(film.getDuration() < 0) {
            log.error("отрицательное число");
            throw new ValidationException("Длительность должна быть положительной");
        }
        if(films.containsKey(film.getId())){
            log.info("фильм {} обновлен", film.getName());
            films.put(film.getId(), film);
        }else{
            log.error("NOT SUCH FILM TO UPDATE");
            System.out.println("NOT SUCH FILM TO UPDATE");
        }
        return film;
    }
}
