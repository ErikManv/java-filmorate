package ru.yandex.practicum.filmorate.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
public class MpaService {
    private MpaDao mpaDao;

    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    public Mpa getMpa(int id) {
        if(id < 0) {
            throw new NullPointerException("Mpa with id " + id + " has not been found!");
        }

        return mpaDao.getMpa(id);
    }
}