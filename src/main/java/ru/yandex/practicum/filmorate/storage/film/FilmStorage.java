package ru.yandex.practicum.filmorate.storage.film;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
     List<Film> findAll();

     Film addFilm(Film film);

     boolean containsFilm(int filmId);

     Film updateFilm(Film film);

     void putLike(int filmId,int userId);

     void deleteLike(int filmId, int userId);

     List<Film> topFilms(int count);
}
