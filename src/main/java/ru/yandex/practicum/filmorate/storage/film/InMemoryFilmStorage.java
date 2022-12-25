package ru.yandex.practicum.filmorate.storage.film;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    int filmId = 0;
    private void countId() {
        filmId++;
    }

    private Map<Integer, Film> films = new HashMap<>();


    @Override
    public List<Film> findAll(){
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film){
        countId();
        film.setId(filmId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean containsFilm(int filmId) {
        return films.containsKey(filmId);
    }

    @Override
    public Film updateFilm(Film film){
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void putLike(int filmId, int userId){
        films.get(filmId).getPersonsLikedFilm().add(userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        films.get(filmId).getPersonsLikedFilm().remove(userId);
    }

    @Override
    public List<Film> topFilms(int count){
       return findAll()
               .stream()
               .sorted(Comparator.comparing(o->o.getPersonsLikedFilm().size()))
               .limit(count)
               .collect(Collectors.toList());
    }
}
