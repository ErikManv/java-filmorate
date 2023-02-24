package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {

    private GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    public Genre getGenre(int id) {
        if(id <= 0) {
            throw new NullPointerException("Genre with id " + id + " has not been found!");
        }
        return genreDao.getGenre(id);
    }
}
