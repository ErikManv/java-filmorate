package ru.yandex.practicum.filmorate.storage.film;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
     List<Film> findAll();

     Film addFilm(Film film);

     boolean containsFilm(Integer filmId);
     
     Film getFilm(Integer filmId);

     Film updateFilm(Film film);

     void putLike(Integer filmId,Integer userId);

     void deleteLike(Integer filmId, Integer userId);

     List<Film> topFilms(Integer count);
}
